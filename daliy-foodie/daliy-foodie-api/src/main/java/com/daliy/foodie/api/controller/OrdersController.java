package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.consts.RedisKeys;
import com.daliy.foodie.common.enums.OrderStatusEnum;
import com.daliy.foodie.common.enums.PayMethod;
import com.daliy.foodie.common.utils.CookieUtils;
import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.JsonUtils;
import com.daliy.foodie.common.utils.RedisOperator;
import com.daliy.foodie.pojo.OrderStatus;
import com.daliy.foodie.pojo.Users;
import com.daliy.foodie.pojo.bo.ShopcartBO;
import com.daliy.foodie.pojo.bo.SubmitOrderBO;
import com.daliy.foodie.pojo.bo.UserBO;
import com.daliy.foodie.pojo.vo.MerchantOrdersVO;
import com.daliy.foodie.pojo.vo.OrderVO;
import com.daliy.foodie.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by perl on 2019-12-07.
 */
@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
@Slf4j
public class OrdersController extends BaseController{


    private static final String SHOP_CART_KEY = "shopcarts";

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
    @PostMapping("/create")
    public JSONResult create(
            @RequestBody SubmitOrderBO submitOrderBO,
            HttpServletRequest request,
            HttpServletResponse response) {

        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
                && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
            return JSONResult.errorMsg("支付方式不支持！");
        }
        // 从redis中获取购物车信息
        String shopCartStr = redisOperator.get(SHOP_CART_KEY);

        if (StringUtils.isBlank(shopCartStr)) {
            return JSONResult.errorMsg("缓存中没有购物车数据,无法下单");
        }

        List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopCartStr,ShopcartBO.class);

        // 1. 创建订单
        OrderVO orderVO = orderService.createOrder(shopcartList,submitOrderBO);
        String orderId = orderVO.getOrderId();

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        shopcartList.removeAll(orderVO.getWaitDelShopCartItems());
        String newShopCartItems = JsonUtils.objectToJson(shopcartList);
        redisOperator.set(SHOP_CART_KEY,newShopCartItems);
        CookieUtils.setCookie(request, response, RedisKeys.FOODIE_SHOPCART, newShopCartItems, true);

        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setReturnUrl(PAY_RETURN_URL);

        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
        merchantOrdersVO.setAmount(1);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("imoocUserId","5134600-1605333199");
        headers.add("password","odlk-pekd-prl2-30ld");

        HttpEntity<MerchantOrdersVO> entity =
                new HttpEntity<>(merchantOrdersVO, headers);

        ResponseEntity<JSONResult> responseEntity =
                restTemplate.postForEntity(PAYMENT_URL,
                        entity,
                        JSONResult.class);
        JSONResult paymentResult = responseEntity.getBody();
        if (paymentResult.getStatus() != 200) {
            log.error("发送错误：{}", paymentResult.getMsg());
            return JSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
        }

        return JSONResult.ok(orderId);
    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId) {
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public JSONResult getPaidOrderInfo(String orderId) {
        OrderStatus orderStatus = orderService.queryOrderStatusInfo(orderId);
        return JSONResult.ok(orderStatus);
    }
}
