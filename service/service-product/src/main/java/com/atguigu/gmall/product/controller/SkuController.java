package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: cxz
 * @create； 2022-08-25 20:42
 **/
@RestController
@RequestMapping("admin/product")
public class SkuController {
    @Autowired
    SkuInfoService skuInfoService;

    /**
     * sku分页查询
     * @param pn
     * @param ps
     * @return
     */
    @GetMapping("/list/{pn}/{ps}")
    public Result getSkuList(@PathVariable("pn")Long pn,
                             @PathVariable("ps")Long ps){
        Page<SkuInfo> page = new Page<>(pn,ps);
        Page<SkuInfo> skuInfoPage = skuInfoService.page(page);
        return Result.ok(skuInfoPage);
    }
    @PostMapping("saveSkuInfo")
    public Result saveSkuInfo(@RequestBody SkuInfo skuInfo){
        skuInfoService.saveSkuInfo(skuInfo);
        return Result.ok();
    }

    /**
     * 商品下架
     * @return
     */
    @GetMapping("cancelSale/{skuId}")
    public Result cancelSale(@PathVariable("skuId")Long skuId){
        skuInfoService.cancelSale(skuId);
        return Result.ok();
    }
    /**
     * 商品上架
     * @return
     */
    @GetMapping("onSale/{skuId}")
    public Result onSale(@PathVariable("skuId")Long skuId){
        skuInfoService.onSale(skuId);
        return Result.ok();
    }

}
