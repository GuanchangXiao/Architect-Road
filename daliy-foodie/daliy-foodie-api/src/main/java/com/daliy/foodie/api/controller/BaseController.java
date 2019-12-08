package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.utils.JSONResult;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

import java.io.File;

/**
 *
 * @Description: controller 基类
 * Created by perl on 11/23/19.
 */
@Controller
public class BaseController {

    public static final String FOODIE_SHOPCART = "shopcart";

    // 支付中心的调用地址
    String PAYMENT_URL = "http://localhost:8088/foodie-payment/payment/createMerchantOrder";

    // 回调通知的url
    String PAY_RETURN_URL = "http://localhost:8088/foodie-dev-api/orders/notifyMerchantOrderPaid";

    // 用户上传头像的位置
    public static final String IMAGE_USER_FACE_LOCATION = File.separator + "Users" +
            File.separator + "perl" +
            File.separator + "Documents" +
            File.separator + "faces";

//    @Autowired
//    public MyOrdersService myOrdersService;

//    /**
//     * 用于验证用户和订单是否有关联关系，避免非法用户调用
//     * @return
//     */
//    public JSONResult checkUserOrder(String userId, String orderId) {
//        Orders order = myOrdersService.queryMyOrder(userId, orderId);
//        if (order == null) {
//            return JSONResult.errorMsg("订单不存在！");
//        }
//        return JSONResult.ok(order);
//    }
}
