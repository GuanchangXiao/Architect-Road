package com.daliy.foodie.dfs.controller;

import com.daliy.foodie.common.consts.RedisKeys;
import com.daliy.foodie.common.utils.CookieUtils;
import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.JsonUtils;
import com.daliy.foodie.common.utils.RedisOperator;
import com.daliy.foodie.pojo.Orders;
import com.daliy.foodie.pojo.Users;
import com.daliy.foodie.pojo.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
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
        redisOperator.set(RedisKeys.USER_TOKEN + ":" + userVO.getId(),userVO.getUserUniqueToken());
        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userVO), true);
    }
}
