package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: cxz
 * @create； 2022-08-23 19:45
 **/
@RestController
@RequestMapping("admin/product")
public class FileUpLoadController {
    @PostMapping("/fileUpload")
    public Result fileUpload(@RequestPart("file")MultipartFile file) {

        return Result.ok();
    }

}
