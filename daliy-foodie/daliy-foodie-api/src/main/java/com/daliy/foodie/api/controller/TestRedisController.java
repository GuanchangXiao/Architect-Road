package com.daliy.foodie.api.controller;

import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试redis
 * Created by perl on 2020-01-04.
 */
@RequestMapping("/test/redis")
@RestController
public class TestRedisController {

//    @Autowired
//    private RedisTemplate redisTemplate;
    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/get/{key}")
    public JSONResult get(@PathVariable String key) {
//        String value = (String) redisTemplate.opsForValue().get(key);
        String value = redisOperator.get(key);
        return JSONResult.ok(value);
    }

    @GetMapping("/set")
    public JSONResult set(String key,String value) {
//        redisTemplate.opsForValue().set(key,value);
        redisOperator.set(key,value);
        return JSONResult.ok();
    }

    @GetMapping("/del/{key}")
    public JSONResult del(@PathVariable String key) {
//        redisTemplate.delete(key);
        redisOperator.del(key);
        return JSONResult.ok();
    }
}
