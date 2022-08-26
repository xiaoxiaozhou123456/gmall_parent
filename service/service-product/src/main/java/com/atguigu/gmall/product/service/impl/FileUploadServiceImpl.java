package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.DateUtil;
import com.atguigu.gmall.product.config.MinIo.MinIoConfig;
import com.atguigu.gmall.product.config.MinIo.MinIoProperties;
import com.atguigu.gmall.product.service.FileUploadService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

/**
 * @author: cxz
 * @create； 2022-08-25 15:06
 **/
@Service
public class FileUploadServiceImpl implements FileUploadService {
    @Autowired
    MinIoConfig minIoConfig;
    @Autowired
    MinIoProperties minIoProperties;
    @Override
    public String fileUpload(MultipartFile file) throws Exception {
        MinioClient minIoClient = minIoConfig.getMinIoClient();
        // 使用putObject上传一个文件到存储桶中。
       String fileName = UUID.randomUUID().toString().replace("-","")+"-"+file.getOriginalFilename();
        String date = DateUtil.formatDate(new Date());
        PutObjectOptions options = new PutObjectOptions(file.getSize(), -1L);
        String contentType = file.getContentType();
        options.setContentType(contentType);
        minIoClient.putObject(minIoProperties.getBucketName(),
                date+"/"+fileName,
                file.getInputStream(),
                options);
        return minIoProperties.getEndpoint()+"/"+minIoProperties.getBucketName()+"/"+date+"/"+fileName;
    }
}
