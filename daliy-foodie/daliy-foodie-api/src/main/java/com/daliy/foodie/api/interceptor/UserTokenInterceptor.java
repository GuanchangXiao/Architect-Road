package com.daliy.foodie.api.interceptor;

import com.daliy.foodie.common.consts.RedisKeys;
import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.JsonUtils;
import com.daliy.foodie.common.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by perl on 2020-01-11.
 */
@Component
@Slf4j
public class UserTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("----进入UserTokenInterceptor拦截器----");
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(userToken)) {
            log.error("用户未登录");
            buildErrorsMsg(response,JSONResult.errorMsg("用户未登陆"));
            return false;
        }else {
            String redisUserToken = redisOperator.get(RedisKeys.USER_TOKEN + ":" + userId);
            if (StringUtils.isBlank(redisUserToken)) {
                log.error("redis中没有token值");
                buildErrorsMsg(response,JSONResult.errorMsg("用户信息错误"));
                return false;
            }
            if (!redisUserToken.equals(userToken)) {
                log.error("token不一致");
                buildErrorsMsg(response,JSONResult.errorMsg("用户信息错误"));
                return false;
            }
        }
        log.info("验证token成功");
        return true;
    }

    /**
     * 返回json格式错误信息
     * @param response
     * @param result
     */
    public void buildErrorsMsg(HttpServletResponse response, JSONResult result) {
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            log.error("write or get OutputStream error : {}",e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    log.error("close OutputStream error : {}",e);
                }
            }
        }
    }
}
