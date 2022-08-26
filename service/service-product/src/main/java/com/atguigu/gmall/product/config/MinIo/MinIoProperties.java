package com.atguigu.gmall.product.config.MinIo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: cxz
 * @create； 2022-08-25 15:07
 **/
@Component
@Data
@ConfigurationProperties(prefix = "app.minio")
public class MinIoProperties {
    private String endpoint;
    private String ak;
    private String sk;
    private String bucketName;

}
