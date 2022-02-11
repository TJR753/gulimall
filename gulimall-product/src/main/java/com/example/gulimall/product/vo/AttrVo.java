package com.example.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrVo {
    private Long attrID;
    private String attrName;
    private Long searchType;
    private Long valueType;
    private String icon;
    private String valueSelect;
    private Integer attrType;
    private Long enable;
    private Long catelogID;
    private Integer showDesc;
    private Long attrGroupID;
}
