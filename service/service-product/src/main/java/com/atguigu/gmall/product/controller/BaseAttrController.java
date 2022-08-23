package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.service.BaseAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: cxz
 * @create； 2022-08-23 16:49
 **/
@RestController
@RequestMapping("/admin/product")
public class BaseAttrController {
    @Autowired
    BaseAttrInfoService baseAttrInfoService;
    @Autowired
    BaseAttrValueService baseAttrValueService;
    /**
     * 根据分类ID查询平台属性
     *
     * @return
     */
    @GetMapping("/attrInfoList/{c1Id}/{c2Id}/{c3Id}")
    public Result getAttrInfoList(@PathVariable("c1Id") Long c1Id,
                                  @PathVariable("c2Id") Long c2Id,
                                  @PathVariable("c3Id") Long c3Id) {
        List<BaseAttrInfo> attrInfoList = baseAttrInfoService.getAttrInfoAndValueByCategoryId(c1Id, c2Id, c3Id);
        return Result.ok(attrInfoList);
    }

    /**
     * 保存平台信息
     *
     * @param Info
     * @return
     */
    @PostMapping("saveAttrInfo")
    public Result saveOrUpdateAttrInfo(@RequestBody BaseAttrInfo Info) {
        baseAttrInfoService.saveOrUpdateAttrInfo(Info);
        return Result.ok();
    }
    /**
     * 根据平台属性ID获取平台属性值数据
     */
    @GetMapping("getAttrValueList/{attrId}")
    public Result getAttrValueList(@PathVariable("attrId")Long attrId){
        List<BaseAttrValue> baseAttrValues = baseAttrValueService.getAttrValueList(attrId);
        return Result.ok(baseAttrValues);
    }

}
