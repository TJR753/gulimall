package com.example.gulimall.search.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {
    //关键字
    private String keyword;
    //分类id
    private Long catalog3Id;
    //排序条件
    //skuPrice,hasStock,hotScore
    private String sort;
    //过滤条件
    private Integer hasStock;
    private String skuPrice;
    private List<Long> brandId;//品牌id
    private List<String> attrs;
    private Integer pageNum;
}
