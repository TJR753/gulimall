package com.example.gulimall.search.controller;

import com.example.common.exception.BizCodeEnum;
import com.example.common.to.es.SkuESModel;
import com.example.common.utils.R;
import com.example.gulimall.search.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/es/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    @PostMapping(path = "/up/save")
    public R up(@RequestBody List<SkuESModel> skuESModelList){
        Boolean success= null;
        try {
            success = searchService.save(skuESModelList);
        } catch (IOException e) {
            log.error("es商品上架有异常",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
        return R.ok().put("success",success);
    }
}
