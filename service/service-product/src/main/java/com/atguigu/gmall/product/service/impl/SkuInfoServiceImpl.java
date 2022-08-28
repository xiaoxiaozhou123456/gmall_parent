package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.*;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.atguigu.gmall.model.to.SkuDetailTo;
import com.atguigu.gmall.product.mapper.BaseCategory3Mapper;
import com.atguigu.gmall.product.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.mapper.SkuInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 */
@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo>
    implements SkuInfoService{
    @Autowired
    SkuAttrValueService skuAttrValueService;
    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    SkuImageService skuImageService;

    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    BaseCategory3Mapper baseCategory3Mapper;
    @Autowired
    SpuSaleAttrService spuSaleAttrService;
    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        //1.保存sku信息
        save(skuInfo);
        Long skuId = skuInfo.getId();
        //2.保存平台属性关联信息
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuId);
        }
        skuAttrValueService.saveBatch(skuAttrValueList);
        //3.保存销售属性关联信息
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
            skuSaleAttrValue.setSkuId(skuId);
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);
        //4.保存sku属性图片信息
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuId);
        }
        skuImageService.saveBatch(skuImageList);
    }

    @Override
    public void cancelSale(Long skuId) {
        skuInfoMapper.updateIsSale(skuId,0);
        //TODO 2、从es中删除这个商品
    }

    @Override
    public void onSale(Long skuId) {
        skuInfoMapper.updateIsSale(skuId,1);
        //TODO 2、从es中添加这个商品
    }

    @Override
    public SkuDetailTo getSkuDetail(Long skuId) {
        SkuDetailTo detailTo = new SkuDetailTo();
        //查询skuinfo信息
        SkuInfo skuInfo = getById(skuId);
        //查询的数据放入SkuDetailTo中
        detailTo.setSkuInfo(skuInfo);
        //查询当前sku中图片列表信息
        List<SkuImage> skuImages = skuImageService.getSkuImage(skuId);
        skuInfo.setSkuImageList(skuImages);
        //查询商品的完整分类信息，base_category1、base_category2、base_category3
        CategoryViewTo categoryViewTo = baseCategory3Mapper.getCategoryView(skuInfo.getCategory3Id());
        detailTo.setCategoryView(categoryViewTo);
        //查询实时价格
        BigDecimal price = skuInfoMapper.get1010Price(skuId);
        detailTo.setPrice(price);
        //(√)4、商品（sku）所属的SPU当时定义的所有销售属性名值组合（固定好顺序）。
        //          spu_sale_attr、spu_sale_attr_value
        //          并标识出当前sku到底spu的那种组合，页面要有高亮框 sku_sale_attr_value
        //查询当前sku对应的spu定义的所有销售属性名和值（固定好顺序）并且标记好当前sku属于哪一种组合
        List<SpuSaleAttr> spuSaleAttrs =
                spuSaleAttrService.getSaleAttrAndValueMarkSku(skuInfo.getSpuId(),skuId);
        detailTo.setSpuSaleAttrList(spuSaleAttrs);
        //(√)5、商品（sku）的所有兄弟产品的销售属性名和值组合关系全部查出来，并封装成
        // {"118|120": "50","119|121": 50} 这样的json字符串
        Long spuId = skuInfo.getSpuId();
        String valuejson = spuSaleAttrService.getAllSkuSaleAttrValueJson(spuId);
        detailTo.setValuesSkuJson(valuejson);
        return detailTo;
    }

    @Override
    public BigDecimal get1010Price(Long skuId) {

       return skuInfoMapper.get1010Price(skuId);
    }
}




