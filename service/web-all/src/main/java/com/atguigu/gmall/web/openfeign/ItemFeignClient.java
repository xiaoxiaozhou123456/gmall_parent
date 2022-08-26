package com.atguigu.gmall.web.openfeign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: cxz
 * @create； 2022-08-26 18:54
 **/

@FeignClient("service-item")
@RequestMapping("/api/inner/rpc/item")
public interface ItemFeignClient {
    @GetMapping("/skudetail/{skuId}")
   Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId")Long skuId);
}
