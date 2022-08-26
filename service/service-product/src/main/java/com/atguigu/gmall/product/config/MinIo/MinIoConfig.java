package com.atguigu.gmall.product.config.MinIo;

import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: cxz
 * @create； 2022-08-25 15:09
 **/
@Configuration
public class MinIoConfig {
    @Autowired
    MinIoProperties minIoProperties;
    @Bean
    public MinioClient getMinIoClient() throws Exception {
        // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
        MinioClient minioClient = new MinioClient(
                minIoProperties.getEndpoint(),
                minIoProperties.getAk(),
                minIoProperties.getSk());

        // 检查存储桶是否已经存在
        boolean isExist = minioClient.bucketExists(minIoProperties.getBucketName());
        if(!isExist) {
            // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
            minioClient.makeBucket(minIoProperties.getBucketName());
        }
        return minioClient;
    }
}
