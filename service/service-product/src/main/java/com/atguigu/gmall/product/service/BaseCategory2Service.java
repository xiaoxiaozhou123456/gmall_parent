package com.atguigu.gmall.product.service;

import com.atguigu.gmall.model.product.BaseCategory2;
import com.atguigu.gmall.model.to.CategoryTreeTo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface BaseCategory2Service extends IService<BaseCategory2> {

    List<BaseCategory2> getCategory2(Long id);

    List<CategoryTreeTo> getAllCategoryWithTree();
}
