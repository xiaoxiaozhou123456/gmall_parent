package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.item.feign.SkuDetailFeignClient;
import com.atguigu.gmall.item.service.SkuDetailService;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@Service
public class SkuDetailServiceImpl implements SkuDetailService {

    @Autowired
    SkuDetailFeignClient skuDetailFeignClient;
    @Autowired
    ThreadPoolExecutor threadPoolExecutor;


    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();
        CompletableFuture<SkuInfo> skuInfoFuture = CompletableFuture.supplyAsync(() -> {
            //1.查询skuinfo信息
            Result<SkuInfo> skuInfo = skuDetailFeignClient.getSkuInfo(skuId);
            detailTo.setSkuInfo(skuInfo.getData());
            return skuInfo.getData();
        },threadPoolExecutor);
        CompletableFuture<Void> skuImagesFuture = skuInfoFuture.thenAcceptAsync(r -> {
            //2.查询sku图片信息
            Result<List<SkuImage>> skuImages = skuDetailFeignClient.getSkuImages(skuId);
            r.setSkuImageList(skuImages.getData());
        }, threadPoolExecutor);

        CompletableFuture<Void> categoryViewFuture = skuInfoFuture.thenAcceptAsync(r -> {
            //3.查询商品的完整分类信息
            Result<CategoryViewTo> categoryView = skuDetailFeignClient.getCategoryView(r.getCategory3Id());
            detailTo.setCategoryView(categoryView.getData());
        }, threadPoolExecutor);

        CompletableFuture<Void> priceFuture = CompletableFuture.runAsync(() -> {
            //4.查询实时价格
            Result<BigDecimal> price = skuDetailFeignClient.get1010Price(skuId);
            detailTo.setPrice(price.getData());
        }, threadPoolExecutor);

        CompletableFuture<Void> saleAttrAndValueMarkSkuFuture = skuInfoFuture.thenAcceptAsync(r -> {
            //5.查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
            Result<List<SpuSaleAttr>> saleAttrAndValueMarkSku = skuDetailFeignClient.getSaleAttrAndValueMarkSku(skuId, r.getSpuId());
            detailTo.setSpuSaleAttrList(saleAttrAndValueMarkSku.getData());
        }, threadPoolExecutor);

        CompletableFuture<Void> allSkuSaleAttrValueJsonFuture = skuInfoFuture.thenAcceptAsync(r -> {
            //6. 商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
            Result<String> allSkuSaleAttrValueJson = skuDetailFeignClient.getAllSkuSaleAttrValueJson(r.getSpuId());
            detailTo.setValuesSkuJson(allSkuSaleAttrValueJson.getData());
        }, threadPoolExecutor);

        CompletableFuture.allOf(skuImagesFuture,categoryViewFuture,priceFuture
        ,saleAttrAndValueMarkSkuFuture,allSkuSaleAttrValueJsonFuture).join();

        return detailTo;
    }
}
