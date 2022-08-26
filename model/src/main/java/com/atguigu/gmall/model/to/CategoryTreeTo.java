package com.atguigu.gmall.model.to;

import lombok.Data;

import java.util.List;

/**
 * @author: cxz
 * @create； 2022-08-26 18:06
 **/
@Data
public class CategoryTreeTo {
    private Long categoryId;
    private String categoryName;
    private List<CategoryTreeTo> categoryChild;//子分类
}
