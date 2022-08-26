package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SpuImage;
import com.atguigu.gmall.model.product.SpuInfo;
import com.atguigu.gmall.product.service.SpuImageService;
import com.atguigu.gmall.product.service.SpuInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: cxz
 * @create； 2022-08-25 19:16
 **/
@RequestMapping("admin/product")
@RestController
public class SpuController {
    @Autowired
    SpuInfoService spuInfoService;
    @Autowired
    SpuImageService spuImageService;

    /**
     * 商品spu分页查询
     * @param pn
     * @param ps
     * @param category3Id
     * @return
     */
    @GetMapping("/{pn}/{ps}")
    public Result getSpuPage(@PathVariable("pn")Long pn,
                             @PathVariable("ps")Long ps,
                             @RequestParam("category3Id")Long category3Id){
        Page<SpuInfo> page = new Page<>(pn,ps);
        LambdaQueryWrapper<SpuInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpuInfo::getCategory3Id,category3Id);
        Page<SpuInfo> infoPage = spuInfoService.page(page, wrapper);
        return Result.ok(infoPage);
    }

    /**
     * 保存spu信息
     * @param spuInfo
     * @return
     */
    @PostMapping("saveSpuInfo")
    public Result saveSpuInfo(@RequestBody SpuInfo spuInfo){
            spuInfoService.saveSpuInfo(spuInfo);
        return Result.ok();
    }

    /**
     * 查询指定spu的商品图片
     * @return
     */
    @GetMapping("/spuImageList/{spuId}")
    public Result spuImageList(@PathVariable("spuId")Long spuId){
        LambdaQueryWrapper<SpuImage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SpuImage::getSpuId,spuId);
        List<SpuImage> list = spuImageService.list(wrapper);
        return Result.ok(list);
    }

}
