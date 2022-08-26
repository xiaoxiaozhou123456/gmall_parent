package com.atguigu.gmall.web.openfeign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: cxz
 * @create； 2022-08-26 18:09
 **/

@FeignClient("service-product")
@RequestMapping("/api/inner/rpc/product")
public interface CategoryFeignClient {
    @GetMapping("/category/tree")
     Result<List<CategoryTreeTo>> getAllCategoryWithTree();
}
