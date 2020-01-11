package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.utils.CookieUtils;
import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.JsonUtils;
import com.daliy.foodie.common.utils.RedisOperator;
import com.daliy.foodie.pojo.Orders;
import com.daliy.foodie.pojo.Users;
import com.daliy.foodie.pojo.vo.UserVO;
import com.daliy.foodie.service.center.MyOrdersService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 *
 * @Description: controller 基类
 * Created by perl on 11/23/19.
 */
@Controller
public class BaseController {

    @Autowired
    private RedisOperator redisOperator;

    public static final String FOODIE_SHOPCART = "shopcart";
    public static final String SHOP_CART_KEY = "shopcarts";
    public static final String USER_TOKEN = "login_users";

    public static final Set<String> FACE_FILE_SET = new HashSet() {
        {
            add("png");
            add("jpg");
            add("jpeg");

        }
    };

    // 支付中心的调用地址
    String PAYMENT_URL = "http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder";		// produce

    // 微信支付成功 -> 支付中心 -> 天天吃货平台
    //                       |-> 回调通知的url
    String PAY_RETURN_URL = "http://api.z.mukewang.com/foodie-dev-api/orders/notifyMerchantOrderPaid";

    // 用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
            File.separator + "perl" +
            File.separator + "Documents" +
            File.separator + "faces";
//    public static final String IMAGE_USER_FACE_LOCATION = "/workspaces/images/foodie/faces";


    @Autowired
    public MyOrdersService myOrdersService;

    /**
     * 用于验证用户和订单是否有关联关系，避免非法用户调用
     * @return
     */
    public JSONResult checkUserOrder(String userId, String orderId) {
        Orders order = myOrdersService.queryMyOrder(userId, orderId);
        if (order == null) {
            return JSONResult.errorMsg("订单不存在！");
        }
        return JSONResult.ok(order);
    }

    /**
     * 绑定UserVO数据
     * @param userBO
     * @return
     */
    public UserVO bindUserVO(Users userBO) {
        String token = UUID.randomUUID().toString().trim();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userBO,userVO);
        userVO.setUserUniqueToken(token);

        return userVO;
    }

    /**
     * 更新用户在redis和cookie中的信息
     * @param users
     * @param request
     * @param response
     */
    public void updateUserInfo(Users users, HttpServletRequest request, HttpServletResponse response) {
        UserVO userVO = bindUserVO(users);
        redisOperator.set(USER_TOKEN + ":" + userVO.getId(),userVO.getUserUniqueToken());
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userVO), true);
    }
}
