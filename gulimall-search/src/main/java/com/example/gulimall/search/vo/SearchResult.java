package com.example.gulimall.search.vo;

import com.example.common.to.es.SkuESModel;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    //所有查询到的信息
    private List<SkuESModel> products;
    //分页信息
    private Integer pageNum;//页码
    private Integer totalPages;//总页数
    private Long total;    //总记录数
    //品牌信息
    private List<BrandVo> brands;
    //分类信息
    private List<CatalogVo> catalogs;
    //属性信息
    private List<AttrVo> attrs;

    @Data
    public static class BrandVo{
        private Long brandId;
        private String brandImg;
        private String brandName;
    }
    @Data
    public static class CatalogVo{
        private Long catalogId;
        private String catalogName;
    }
    @Data
    public static class AttrVo{
        private Long attrId;
        private String attrName;
        private List<String> attrValue;
    }
}
