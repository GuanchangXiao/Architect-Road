package com.foodie.order.controller;

import com.foodie.component.RedisOperator;
import com.foodie.enums.OrderStatusEnum;
import com.foodie.order.pojo.OrderStatus;
import com.foodie.order.service.OrderService;
import com.foodie.pojo.JSONResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Api(value = "订单相关", tags = {"订单相关的api接口"})
@RequestMapping("orders")
@RestController
@Slf4j
public class OrdersController extends OrderBaesController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisOperator redisOperator;

    // TODO 依赖购物车模块
//    @ApiOperation(value = "用户下单", notes = "用户下单", httpMethod = "POST")
//    @PostMapping("/create")
//    public JSONResult create(
//            @RequestBody SubmitOrderBO submitOrderBO,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//
//        if (submitOrderBO.getPayMethod() != PayMethod.WEIXIN.type
//            && submitOrderBO.getPayMethod() != PayMethod.ALIPAY.type ) {
//            return JSONResult.errorMsg("支付方式不支持！");
//        }
//
////        System.out.println(submitOrderBO.toString());
//
//        String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId());
//        if (StringUtils.isBlank(shopcartJson)) {
//            return JSONResult.errorMsg("购物数据不正确");
//        }
//
//        List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
//
//        // 1. 创建订单
//        OrderVO orderVO = orderService.createOrder(shopcartList, submitOrderBO);
//        String orderId = orderVO.getOrderId();
//
//        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
//        /**
//         * 1001
//         * 2002 -> 用户购买
//         * 3003 -> 用户购买
//         * 4004
//         */
//        // 清理覆盖现有的redis汇总的购物数据
//        shopcartList.removeAll(orderVO.getToBeRemovedShopcatdList());
//        redisOperator.set(FOODIE_SHOPCART + ":" + submitOrderBO.getUserId(), JsonUtils.objectToJson(shopcartList));
//        // 整合redis之后，完善购物车中的已结算商品清除，并且同步到前端的cookie
//        CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartList), true);
//
//        // 3. 向支付中心发送当前订单，用于保存支付中心的订单数据
//        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
//        merchantOrdersVO.setReturnUrl(payReturnUrl);
//
//        // 为了方便测试购买，所以所有的支付金额都统一改为1分钱
//        merchantOrdersVO.setAmount(1);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("imoocUserId","imooc");
//        headers.add("password","imooc");
//
//        HttpEntity<MerchantOrdersVO> entity =
//                new HttpEntity<>(merchantOrdersVO, headers);
//
//        ResponseEntity<JSONResult> responseEntity =
//                restTemplate.postForEntity(paymentUrl,
//                                            entity,
//                                            JSONResult.class);
//        JSONResult paymentResult = responseEntity.getBody();
//        if (paymentResult.getStatus() != 200) {
//            logger.error("发送错误：{}", paymentResult.getMsg());
//            return JSONResult.errorMsg("支付中心订单创建失败，请联系管理员！");
//        }
//
//        return JSONResult.ok(orderId);
//    }

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
