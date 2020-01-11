package com.daliy.foodie.api.controller.center;

import com.daliy.foodie.api.controller.BaseController;
import com.daliy.foodie.api.upload.FileUpload;
import com.daliy.foodie.common.utils.DateUtil;
import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.common.utils.RedisOperator;
import com.daliy.foodie.pojo.Users;
import com.daliy.foodie.pojo.bo.CenterUserBO;
import com.daliy.foodie.service.center.CenterUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by perl on 2019-12-08.
 */
@Api(value = "用户信息接口", tags = {"用户信息相关接口"})
@RestController
@RequestMapping("userInfo")
@Slf4j
public class CenterUserController extends BaseController {

    @Autowired
    private CenterUserService centerUserService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private FileUpload fileUpload;

    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) {
        String faceUrl = "";
        centerUserService.updateUserFace(userId,faceUrl);

        String fileSpace = fileUpload.getImageUserFaceLocation();
        // 在路径上为每一个用户增加一个userid，用于区分不同用户上传
        String uploadPathPrefix = File.separator + userId;

        if (file != null) {
            FileOutputStream fileOutputStream = null;

            try {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNoneBlank(fileName)) {
                    // 获取文件名参数
                    String fileNameArgs[] = fileName.split("\\.");
                    // 获取后缀
                    String fileSuffix = fileNameArgs[fileNameArgs.length - 1];
                    // 判断文件类型是否是图片 (png/jpg/jpeg)
                    if (!FACE_FILE_SET.contains(fileSuffix)) {
                        return JSONResult.errorMsg("无法上传不支持的文件类型");
                    }
                    // 组装文件名   eg: face-{userid}.png
                    String newFileName = "face-" + userId + "." + fileSuffix;
                    String finalFilePath = fileSpace + uploadPathPrefix + File.separator + newFileName;
                    uploadPathPrefix += ("/" + newFileName);
                    File outFile = new File(finalFilePath);
                    if (outFile.getParentFile() != null) {
                        outFile.getParentFile().mkdirs();
                    }

                    fileOutputStream = new FileOutputStream(outFile);
                    InputStream inputStream = file.getInputStream();
                    IOUtils.copy(inputStream,fileOutputStream);
                }
            } catch (IOException e) {
                log.error("{} : Upload File Error: {}",DateUtil.getCurrentDateTime(),e);
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    log.error("{} : Close fileOutputStream Error: {}",DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN),e);
                }
                log.info("{} : Close fileOutputStream Done...", DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN));
            }
        }else {
            return JSONResult.errorMsg("上传文件为空");
        }

        // 获取图片服务地址
        String imageServerUrl = fileUpload.getImageServerUrl();

        // 由于浏览器可能存在缓存的情况，所以在这里，我们需要加上时间戳来保证更新后的图片可以及时刷新
        String finalUserFaceUrl = imageServerUrl + uploadPathPrefix
                + "?t=" + DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        updateUserInfo(userResult,request,response);
        return JSONResult.ok();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息", httpMethod = "POST")
    @PostMapping("update")
    public JSONResult update(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @RequestBody @Valid CenterUserBO centerUserBO,
            BindingResult result,
            HttpServletRequest request, HttpServletResponse response) {
        log.info("update User : {}",centerUserBO);

        // 判断BindingResult是否保存错误的验证信息，如果有，则直接return
        if (result.hasErrors()) {
            Map<String, String> errorMap = getErrors(result);
            return JSONResult.errorMap(errorMap);
        }

        Users userResult = centerUserService.updateUserInfo(userId, centerUserBO);
        updateUserInfo(userResult,request,response);

        return JSONResult.ok();

    }

    /**
     * 获取错误字段信息
     * @param result
     * @return
     */
    private Map<String, String> getErrors(BindingResult result) {
        Map<String, String> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            // 发生验证错误所对应的某一个属性
            String errorField = error.getField();
            // 验证错误的信息
            String errorMsg = error.getDefaultMessage();

            map.put(errorField, errorMsg);
        }
        return map;
    }
}
