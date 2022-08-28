package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.common.util.Jsons;
import com.atguigu.gmall.model.product.SpuSaleAttr;
import com.atguigu.gmall.model.to.ValueSkuJsonTo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.SpuSaleAttrService;
import com.atguigu.gmall.product.mapper.SpuSaleAttrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Service
public class SpuSaleAttrServiceImpl extends ServiceImpl<SpuSaleAttrMapper, SpuSaleAttr>
    implements SpuSaleAttrService{
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;

    @Override
    public List<SpuSaleAttr> getSaleAttrAndValueMarkSku(Long spuId, Long skuId) {

       return spuSaleAttrMapper.getSaleAttrAndValueMarkSku(spuId,skuId);
    }

    @Override
    public List<SpuSaleAttr> getSaleAttrAndValueBySpuId(Long spuId) {
        return spuSaleAttrMapper.getSaleAttrAndValueBySpuId(spuId);

    }

    @Override
    public String getAllSkuSaleAttrValueJson(Long spuId) {

       List<ValueSkuJsonTo> list =  spuSaleAttrMapper.getAllSkuSaleAttrValueJson(spuId);
        Map<String,Long> map = new HashMap<>();
        for (ValueSkuJsonTo valueSkuJsonTo : list) {
            String valueJson = valueSkuJsonTo.getValueJson();
            Long skuId = valueSkuJsonTo.getSkuId();
            map.put(valueJson,skuId);
        }
        String toStr = Jsons.toStr(map);
        return toStr;
    }
}




