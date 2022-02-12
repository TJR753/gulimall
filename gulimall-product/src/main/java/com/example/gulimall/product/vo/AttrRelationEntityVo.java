package com.example.gulimall.product.vo;// AttrRelationVO.java
import lombok.Data;
import java.util.List;


@Data
public class AttrRelationEntityVo {
    private Long attrGroupId;
    private String attrGroupName;
    private Integer sort;
    private String descript;
    private String icon;
    private Long catelogId;
    private List<AttrVo> attrs;
}

