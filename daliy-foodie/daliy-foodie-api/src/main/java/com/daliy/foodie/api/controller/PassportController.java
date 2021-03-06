package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.consts.RedisKeys;
import com.daliy.foodie.common.utils.*;
import com.daliy.foodie.pojo.Users;
import com.daliy.foodie.pojo.bo.UserBO;
import com.daliy.foodie.pojo.vo.UserVO;
import com.daliy.foodie.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Created by perl on 2019-12-07.
 */
@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @ApiOperation(value = "用户名是否存在", notes = "用户名是否存在", httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username) {

        // 1. 判断用户名不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }

        // 2. 查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }

        // 3. 请求成功，用户名没有重复
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPwd = userBO.getConfirmPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password) ||
                StringUtils.isBlank(confirmPwd)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 查询用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }

        // 2. 密码长度不能少于6位
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6");
        }

        // 3. 判断两次密码是否一致
        if (!password.equals(confirmPwd)) {
            return JSONResult.errorMsg("两次密码输入不一致");
        }

        // 4. 实现注册
        Users userResult = userService.createUser(userBO);
        updateUserInfo(userResult,request,response);
        // 同步购物车数据
        syncShopCartData(request,response);
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }

        // 1. 实现登录
        Users userResult = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));

        if (userResult == null) {
            return JSONResult.errorMsg("用户名或密码不正确");
        }
        // 更新用户信息
        updateUserInfo(userResult,request,response);
        // 同步购物车数据
        syncShopCartData(request,response);
        return JSONResult.ok(userResult);
    }

    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");
        // 用户退出登录，需要清空购物车
        CookieUtils.deleteCookie(request, response, RedisKeys.FOODIE_SHOPCART);

        // 清除redis用户数据
        redisOperator.del(RedisKeys.USER_TOKEN + ":" + userId);

        return JSONResult.ok();
    }

    /**
     * 同步cookie和redis购物车中的数据
     * @param request
     * @param response
     */
    private void syncShopCartData(HttpServletRequest request, HttpServletResponse response) {
        String redisData = redisOperator.get(RedisKeys.SHOP_CART_KEY);
        String cookieData = CookieUtils.getCookieValue(request,RedisKeys.FOODIE_SHOPCART,true);

        if (StringUtils.isNotBlank(cookieData)) {
            if (StringUtils.isBlank(redisData)) {
                redisOperator.set(RedisKeys.SHOP_CART_KEY,cookieData);
            }else {
                // redis和cookie都有数据，此时应该合并两边的数据
            }
        }else {
            if (StringUtils.isNotBlank(redisData)) {
                CookieUtils.setCookie(request,response,RedisKeys.FOODIE_SHOPCART,redisData,true);
            }
        }
    }
}
