package com.atguigu.gmall.product.api;


import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.SkuImage;
import com.atguigu.gmall.model.product.SkuInfo;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.service.BaseCategory3Service;
import com.atguigu.gmall.product.service.SkuImageService;
import com.atguigu.gmall.product.service.SkuInfoService;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


/**
 * 商品详情数据库层操作
 */
@RequestMapping("/api/inner/rpc/product")
@RestController
public class SkuDetaiApiController {


    @Autowired
    SkuInfoService skuInfoService;
    @Autowired
    SkuImageService skuImageService;
    @Autowired
    BaseCategory3Service baseCategory3Service;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;
//    /**
//     * 数据库层真正查询商品详情
//     * @param skuId
//     * @return
////     */
//    @GetMapping("/skudetail/{skuId}")
//    public Result<SkuDetailTo> getSkuDetail(@PathVariable("skuId") Long skuId){
//        //准备查询所有需要的数据
//        SkuDetailTo skuDetailTo = skuInfoService.getSkuDetail(skuId);
//        return Result.ok(skuDetailTo);
//    }

    //1

    /**
     * 查询skuinfo信息
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/skuInfo/{skuId}")
    public Result<SkuInfo> getSkuInfo(@PathVariable("skuId")Long skuId){
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return Result.ok(skuInfo);
    }
    /**
     * 查询sku图片信息
     * @param skuId
     * @return
     */
    @GetMapping("/skudetail/SkuImages/{skuId}")
    public Result<List<SkuImage>> getSkuImages(@PathVariable("skuId")Long skuId){
        List<SkuImage> skuImages = skuImageService.getSkuImage(skuId);
        return Result.ok(skuImages);
    }
    /**
     * 查询商品的完整分类信息
     * @param
     * @return
     */
    @GetMapping("/skudetail/CategoryView/{category3Id}")
    public Result<CategoryViewTo> getCategoryView(@PathVariable("category3Id")Long category3Id){
        //查询商品的完整分类信息，base_category1、base_category2、base_category3
        CategoryViewTo categoryViewTo = baseCategory3Service.getCategoryView(category3Id);
        return Result.ok(categoryViewTo);
    }
    /**
     * 查询实时价格
     * @param
     * @return
     */
    @GetMapping("/skudetail/get1010Price/{skuId}")
    public Result<BigDecimal> get1010Price(@PathVariable("skuId")Long skuId){
        //查询实时价格
        BigDecimal price = skuInfoService.get1010Price(skuId);
        return Result.ok(price);
    }
    /**
     * 查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
     * @param
     * @return
     */
    @GetMapping("/skudetail/getSaleAttrAndValueMarkSku/{spuId}/{skuId}")
    public Result<List<SpuSaleAttr>> getSaleAttrAndValueMarkSku(@PathVariable("skuId")Long skuId,
                                                  @PathVariable("spuId")Long spuId){
        //(√)4、商品（sku）所属的SPU当时定义的所有销售属性名值组合（固定好顺序）。
        //          spu_sale_attr、spu_sale_attr_value
        //          并标识出当前sku到底spu的那种组合，页面要有高亮框 sku_sale_attr_value
        //查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
        List<SpuSaleAttr> spuSaleAttrs =
                spuSaleAttrService.getSaleAttrAndValueMarkSku(spuId,skuId);
        return Result.ok(spuSaleAttrs);
    }
    /**
     * 商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
     * @param
     * @return
     */
    @GetMapping("/skudetail/getAllSkuSaleAttrValueJson/{spuId}")
    public Result<String> getAllSkuSaleAttrValueJson(@PathVariable("spuId")Long spuId){
        //(√)5、商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
        // {"118|120": "50","119|121": 50} 这样的json字符串

        String valuejson = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        return Result.ok(valuejson);
    }
}
