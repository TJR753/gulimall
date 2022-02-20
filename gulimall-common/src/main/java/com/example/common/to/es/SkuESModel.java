package com.example.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SkuESModel {
    private Long brandId;
    private String brandImg;
    private String brandName;
    private Long catalogId;
    private String catalogName;
    private Boolean hasStock;
    private Long hotScore;
    private Long saleCount;
    private Long skuId;
    private String skuImg;
    private BigDecimal skuPrice;
    private String skuTitle;
    private Long spuId;
    private List<Attrs> attrs;

    @Data
    public static class Attrs{
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
