package com.example.gulimall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SpuSaveVo {
    private String spuName;
    private String spuDescription;
    private Long catalogID;
    private Long brandID;
    private BigDecimal weight;
    private Integer publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttr> baseAttrs;
    private List<Skus> skus;
}
