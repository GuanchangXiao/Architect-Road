package com.daliy.foodie.dfs.service.impl;

import com.daliy.foodie.dfs.service.DfsService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by perl on 2020-02-09.
 */
@Service
public class DfsServiceImpl implements DfsService {
    @Autowired
    private FastFileStorageClient client;

    @Override
    public String upload(MultipartFile file,String fileExtName) throws Exception {
        StorePath storePath = client.uploadFile(
                file.getInputStream(),
                file.getSize(),
                fileExtName,
                null);

        String path = storePath.getFullPath();
        return path;
    }
}
