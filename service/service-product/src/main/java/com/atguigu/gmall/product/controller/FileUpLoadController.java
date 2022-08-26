package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.product.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: cxz
 * @create； 2022-08-23 19:45
 **/
@RestController
@RequestMapping("admin/product")
public class FileUpLoadController {

    @Autowired
    FileUploadService fileUploadService;
    /**
     *  文件上传Minio
     * @param file
     * @return
     */
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file) throws Exception {
        String url = fileUploadService.fileUpload(file);
        return Result.ok(url);
    }

}
