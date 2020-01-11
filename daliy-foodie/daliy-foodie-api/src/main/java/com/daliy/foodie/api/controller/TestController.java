package com.daliy.foodie.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by perl on 2020-01-11.
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
