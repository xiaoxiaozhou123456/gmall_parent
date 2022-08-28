package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;


@RequestMapping("/api/inner/rpc/product")
@FeignClient("service-product")
public interface SkuDetailFeignClient {

//    @GetMapping("/skudetail/{skuId}")
//    Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId);
    /**
     * 查询skuinfo信息
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/skuInfo/{skuId}")
    Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId);
    /**
     * 查询sku图片信息
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/SkuImages/{skuId}")
    Result<List<SkuImage>> getSkuImages(@PathVariable("skuId")Long skuId);
    /**
     * 查询商品的完整分类信息
     * @param
     * @return
     */
    @GetMapping("/skudetail/CategoryView/{category3Id}")
    Result<CategoryViewTo> getCategoryView(@PathVariable("category3Id")Long category3Id);
    /**
     * 查询实时价格
     * @param
     * @return
     */
    @GetMapping("/skudetail/get1010Price/{skuId}")
    Result<BigDecimal> get1010Price(@PathVariable("skuId")Long skuId);
    /**
     * 查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
     * @param
     * @return
     */
    @GetMapping("/skudetail/getSaleAttrAndValueMarkSku/{spuId}/{skuId}")
    Result<List<SpuSaleAttr>> getSaleAttrAndValueMarkSku(@PathVariable("skuId")Long skuId,
                                                                @PathVariable("spuId")Long spuId);
    /**
     * 商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
     * @param
     * @return
     */
    @GetMapping("/skudetail/getAllSkuSaleAttrValueJson/{spuId}")
    Result<String> getAllSkuSaleAttrValueJson(@PathVariable("spuId")Long spuId);
}
