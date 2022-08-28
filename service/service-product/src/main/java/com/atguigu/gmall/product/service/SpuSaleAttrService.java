package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface SpuSaleAttrService extends IService<SpuSaleAttr> {

    List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId);

    List<SpuSaleAttr> getSaleAttrAndValueBySpuId(Long spuId);

    String getAllSkuSaleAttrValueJson(Long spuId);
}
