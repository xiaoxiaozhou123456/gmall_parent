package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{
    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;

    @Override
    public List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id) {
      return   baseAttrInfoMapper.getAttrInfoAndValueByCategoryId(c1Id,c2Id,c3Id);

    }

    @Override
    public void saveOrUpdateAttrInfo(BaseAttrInfo info) {
        if (info.getId() == null){
            //保存平台属性信息
            baseAttrInfoMapper.insert(info);
            List<BaseAttrValue> attrValueList = info.getAttrValueList();
            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(info.getId());
                baseAttrValueMapper.insert(baseAttrValue);
            }
        }else {
            //修改平台属性，先删除前台删除的平台属性
            List<BaseAttrValue> attrValueList = info.getAttrValueList();
            List<Long> ids = new ArrayList<>();
            for (BaseAttrValue baseAttrValue : attrValueList) {
                Long id = baseAttrValue.getId();
                if (id != null){
                    ids.add(id);
                }
            }
            if (ids.size()>0){
                QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
                deleteWrapper.eq("attr_id",info.getId());
                deleteWrapper.notIn("id",ids);
                baseAttrValueMapper.delete(deleteWrapper);
            }else {
                //全部删除
                QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
                deleteWrapper.eq("attr_id",info.getId());
                baseAttrValueMapper.delete(deleteWrapper);
            }
            for (BaseAttrValue baseAttrValue : attrValueList) {
                if (baseAttrValue.getId() == null){
                    //新增
                    baseAttrValue.setAttrId(info.getId());
                    baseAttrValueMapper.insert(baseAttrValue);
                }else {
                    baseAttrValueMapper.updateById(baseAttrValue);
                }
            }
        }
    }
}




