package com.example.gulimall.product.vo;

import com.example.common.to.MemberPrice;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Skus {
    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Image> images;
    private List<String> descar;
    private Integer fullCount;
    private BigDecimal discount;
    private Integer countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private BigDecimal priceStatus;
    private List<MemberPrice> memberPrice;
}
