package com.example.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catalog2JsonVO {
    //Map<String,List>
    //{"1":[{"catalog1Id":"1","catalog3List":[Object{3},Object{3},], "id":"1","name":"电子书刊"}]}
    private String catalog1Id;//一级分类id
    private List<Catalog3JsonVO> catalog3List;
    private String id;//二级分类id
    private String name;//二级分类名字

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catalog3JsonVO{
        private String catalog2Id;//二级分类id
        private String id;//三级分类id
        private String name;//三级分类名字
    }
}
