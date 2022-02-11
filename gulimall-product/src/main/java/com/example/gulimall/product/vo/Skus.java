package com.example.gulimall.product.vo;

import java.math.BigDecimal;
import java.util.List;

public class Skus {
    private List<Attr> attr;
    private String skuName;
    private String price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Image> images;
    private List<String> descar;
    private BigDecimal fullCount;
    private BigDecimal discount;
    private Long countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private BigDecimal priceStatus;
    private List<MemberPrice> memberPrice;
}
