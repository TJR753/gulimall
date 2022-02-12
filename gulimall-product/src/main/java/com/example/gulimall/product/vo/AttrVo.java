package com.example.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrVo {
    private Long attrId;
    private String attrName;
    private Long searchType;
    private Long valueType;
    private String icon;
    private String valueSelect;
    private Integer attrType;
    private Long enable;
    private Long catelogId;
    private Integer showDesc;
    private Long attrGroupId;
}
