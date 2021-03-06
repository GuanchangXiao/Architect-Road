package com.foodie.user.controller;

import com.foodie.pojo.JSONResult;
import com.foodie.user.UserApplicationProperties;
import com.foodie.user.pojo.Users;
import com.foodie.user.pojo.bo.UserBO;
import com.foodie.user.service.UserService;
import com.foodie.utils.CookieUtils;
import com.foodie.utils.JsonUtils;
import com.foodie.utils.MD5Utils;
import com.foodie.component.RedisOperator;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "注册登录", tags = {"用于注册登录的相关接口"})
@RestController
@RequestMapping("passport")
@Slf4j
public class PassportController extends UserBaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserApplicationProperties properties;

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

//        使用spring config 外部化配置动态开启服务
        if (!properties.isEnabledRegistration()) {
            log.info("service blocked...");
            return JSONResult.errorMsg("服务器繁忙, 请稍后再试");
        }

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

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);

        // TODO 生成用户token，存入redis会话
        // 同步购物车数据
//        synchShopcartData(userResult.getId(), request, response);

        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录", notes = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    @HystrixCommand(
            commandKey = "loginFail",  //全局标识
            groupKey = "password",  //全局服务分组
            fallbackMethod = "loginFail"  // 指定发生错误时 执行的方法 必须在同一个类中 public or private
//            threadPoolKey = "thredPollLogin", // 指定线程组 多个服务可以共用一个线程组
//            threadPoolProperties = {
//                    @HystrixProperty(name = "coreSize", value = "20"),
//                    @HystrixProperty(name = "maxQueueSize", value = "40"),
//                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
//            }
//            ignoreExceptions = {
//                    IllegalArgumentException.class // 指定需要排除的异常，即就算发生这种异常也不会触发Hystrix降级策略
//            }
    )
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

        userResult = setNullProperty(userResult);

        CookieUtils.setCookie(request, response, "user",
                JsonUtils.objectToJson(userResult), true);


        // TODO 生成用户token，存入redis会话
        // 同步购物车数据
//        synchShopcartData(userResult.getId(), request, response);

        return JSONResult.ok(userResult);
    }

    /**
     * 注册登录成功后，同步cookie和redis中的购物车数据
     */
    // TODO 后续放入购物车模块
//    private void synchShopcartData(String userId, HttpServletRequest request,
//                                   HttpServletResponse response) {
//
//        /**
//         * 1. redis中无数据，如果cookie中的购物车为空，那么这个时候不做任何处理
//         *                 如果cookie中的购物车不为空，此时直接放入redis中
//         * 2. redis中有数据，如果cookie中的购物车为空，那么直接把redis的购物车覆盖本地cookie
//         *                 如果cookie中的购物车不为空，
//         *                      如果cookie中的某个商品在redis中存在，
//         *                      则以cookie为主，删除redis中的，
//         *                      把cookie中的商品直接覆盖redis中（参考京东）
//         * 3. 同步到redis中去了以后，覆盖本地cookie购物车的数据，保证本地购物车的数据是同步最新的
//         */
//
//        // 从redis中获取购物车
//        String shopcartJsonRedis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
//
//        // 从cookie中获取购物车
//        String shopcartStrCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);
//
//        if (StringUtils.isBlank(shopcartJsonRedis)) {
//            // redis为空，cookie不为空，直接把cookie中的数据放入redis
//            if (StringUtils.isNotBlank(shopcartStrCookie)) {
//                redisOperator.set(FOODIE_SHOPCART + ":" + userId, shopcartStrCookie);
//            }
//        } else {
//            // redis不为空，cookie不为空，合并cookie和redis中购物车的商品数据（同一商品则覆盖redis）
//            if (StringUtils.isNotBlank(shopcartStrCookie)) {
//
//                /**
//                 * 1. 已经存在的，把cookie中对应的数量，覆盖redis（参考京东）
//                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
//                 * 3. 从cookie中清理所有的待删除list
//                 * 4. 合并redis和cookie中的数据
//                 * 5. 更新到redis和cookie中
//                 */
//
//                List<ShopcartBO> shopcartListRedis = JsonUtils.jsonToList(shopcartJsonRedis, ShopcartBO.class);
//                List<ShopcartBO> shopcartListCookie = JsonUtils.jsonToList(shopcartStrCookie, ShopcartBO.class);
//
//                // 定义一个待删除list
//                List<ShopcartBO> pendingDeleteList = new ArrayList<>();
//
//                for (ShopcartBO redisShopcart : shopcartListRedis) {
//                    String redisSpecId = redisShopcart.getSpecId();
//
//                    for (ShopcartBO cookieShopcart : shopcartListCookie) {
//                        String cookieSpecId = cookieShopcart.getSpecId();
//
//                        if (redisSpecId.equals(cookieSpecId)) {
//                            // 覆盖购买数量，不累加，参考京东
//                            redisShopcart.setBuyCounts(cookieShopcart.getBuyCounts());
//                            // 把cookieShopcart放入待删除列表，用于最后的删除与合并
//                            pendingDeleteList.add(cookieShopcart);
//                        }
//
//                    }
//                }
//
//                // 从现有cookie中删除对应的覆盖过的商品数据
//                shopcartListCookie.removeAll(pendingDeleteList);
//
//                // 合并两个list
//                shopcartListRedis.addAll(shopcartListCookie);
//                // 更新到redis和cookie
//                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, JsonUtils.objectToJson(shopcartListRedis), true);
//                redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartListRedis));
//            } else {
//                // redis不为空，cookie为空，直接把redis覆盖cookie
//                CookieUtils.setCookie(request, response, FOODIE_SHOPCART, shopcartJsonRedis, true);
//            }
//
//        }
//    }



    @ApiOperation(value = "用户退出登录", notes = "用户退出登录", httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult logout(@RequestParam String userId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 清除用户的相关信息的cookie
        CookieUtils.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // 分布式会话中需要清除用户数据
        CookieUtils.deleteCookie(request, response, FOODIE_SHOPCART);

        return JSONResult.ok();
    }


    public JSONResult loginFail(UserBO userBO,
                                HttpServletRequest request,
                                HttpServletResponse response,
                                Throwable throwable) throws Exception{
        log.error(throwable.getMessage());
        return JSONResult.errorMsg("用户名密码错误");
    }
}
