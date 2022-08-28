package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory3;
import com.atguigu.gmall.model.to.CategoryViewTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseCategory3Service extends IService<BaseCategory3> {

    List<BaseCategory3> getCategory3(Long id);

    CategoryViewTo getCategoryView(Long category3Id);
}
