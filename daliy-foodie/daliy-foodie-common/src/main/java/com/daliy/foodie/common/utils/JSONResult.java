package com.daliy.foodie.common.utils;

import com.daliy.foodie.common.enums.HttpResponseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

/**
 * 
 * @Title: JSONResult.java
 * @Package com.imooc.utils
 * @Description: 自定义响应数据结构
 * 				本类可提供给 H5/ios/安卓/公众号/小程序 使用
 * 				前端接受此类数据（json object)后，可自行根据业务去实现相关功能
 * 
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 * 				556: 用户qq校验异常
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class JSONResult {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer status;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    
    @JsonIgnore
    private String ok;	// 不使用

    public JSONResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public JSONResult(Object data) {
        this.status = HttpResponseStatus.SUCCESS.code;
        this.msg = HttpResponseStatus.SUCCESS.message;
        this.data = data;
    }

    public static JSONResult ok(Object data) {
        return new JSONResult(data);
    }

    public static JSONResult ok() {
        return new JSONResult(null);
    }

    public static JSONResult errorMsg(String msg) {
        return new JSONResult(HttpResponseStatus.MSG_ERROR.code, msg, null);
    }

    public static JSONResult errorMap(Object data) {
        return new JSONResult(HttpResponseStatus.MAP_ERROR.code, HttpResponseStatus.MAP_ERROR.message, data);
    }

    public static JSONResult errorTokenMsg(String msg) {
        return new JSONResult(HttpResponseStatus.TOKEN_ERROR.code, msg, null);
    }

    public static JSONResult errorException(String msg) {
        return new JSONResult(HttpResponseStatus.EXCEPTION_ERROR.code, msg, null);
    }

    public static JSONResult errorUserQQ(String msg) {
        return new JSONResult(HttpResponseStatus.EXCEPTION_ERROR.code, msg, null);
    }

    public Boolean isOK() {
        return this.status == HttpResponseStatus.SUCCESS.code;
    }

}
