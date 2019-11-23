package com.daliy.foodie.api.excpetion;

import com.daliy.foodie.common.utils.JSONResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Created by perl on 11/23/19.
 */
@RestControllerAdvice
public class CustomExceptionHandler {

    /**
     * @Description 最大上传文件大小500k,超过该大小则返回异常信息响应
     * @return
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public JSONResult handleMaxUploadFile() {
        return JSONResult.errorMsg("文件上传大小不能超过500k，请压缩图片或者降低图片质量再上传！");
    }
}
