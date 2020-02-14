package com.daliy.foodie.dfs.controller;

import com.daliy.foodie.common.utils.JSONResult;
import com.daliy.foodie.dfs.config.DfsConfig;
import com.daliy.foodie.dfs.service.DfsService;
import com.daliy.foodie.pojo.Users;
import com.daliy.foodie.service.center.CenterUserService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by perl on 2020-02-09.
 */
@RestController
@RequestMapping("upload")
@Slf4j
public class UploadController extends BaseController {

    @Autowired
    private DfsConfig dfsConfig;
    @Autowired
    private DfsService dfsService;
    @Autowired
    private CenterUserService centerUserService;

    @PostMapping("uploadFace")
    public JSONResult uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        String path = "";
        if (file != null) {
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
                path = dfsService.upload(file,fileSuffix);
                log.info("upload success to path : {}",path);
            }

        }else {
            return JSONResult.errorMsg("上传文件为空");
        }

        if (StringUtils.isBlank(path)) {
            return JSONResult.errorMsg("上传图片失败");
        }

        String finalUserFaceUrl = dfsConfig.getHost() + path;
        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        updateUserInfo(userResult,request,response);
        return JSONResult.ok();
    }
}
