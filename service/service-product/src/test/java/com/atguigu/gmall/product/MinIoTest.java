package com.atguigu.gmall.product;

import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.util.concurrent.CompletableFuture;

/**
 * @author: cxz
 * @create； 2022-08-25 15:11
 **/

public class MinIoTest {
    @Test
    public void test() throws Exception{
        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient("http://192.168.200.100:9000",
                    "admin",
                    "admin123456");

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists("gmall");
            if(isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket("gmall");
            }
            FileInputStream file = new FileInputStream("D:\\尚硅谷资料\\尚品汇\\尚品汇\\资料\\03 商品图片\\商品图片\\小米CC9\\仙女渐变色/1.png");
            int available = file.available();

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject("gmall",
                    "1.png",
                    file,
                    new PutObjectOptions(available,-1L));
            System.out.println("/home/user/Photos/asiaphotos.zip is successfully uploaded as asiaphotos.zip to `asiatrip` bucket.");
        } catch(MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void main1(String[] args) {
        CompletableFuture.supplyAsync(()->{
            return 2;
        }).thenApplyAsync(r->{
            System.out.println(r);
            return r*6;
        });
    }
}
