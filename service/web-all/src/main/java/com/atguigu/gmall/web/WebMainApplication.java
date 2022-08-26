package com.atguigu.gmall.web;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: cxz
 * @create； 2022-08-26 17:46
 **/
@SpringCloudApplication
@EnableFeignClients
public class WebMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebMainApplication.class,args);
    }
}
