package com.foodie.user.controller;

import com.foodie.controller.BaseController;
import com.foodie.user.pojo.Users;

/**
 * Created by perl on 2020-03-07.
 */
public class UserBaseController extends BaseController {
    public Users setNullProperty(Users userResult) {
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setCreatedTime(null);
        userResult.setUpdatedTime(null);
        userResult.setBirthday(null);
        return userResult;
    }
}
