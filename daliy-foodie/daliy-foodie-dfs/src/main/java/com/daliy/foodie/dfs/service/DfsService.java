package com.daliy.foodie.dfs.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by perl on 2020-02-09.
 */
public interface DfsService {

    String upload(MultipartFile file, String fileExtName) throws Exception;
}
