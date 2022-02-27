package com.example.gulimall.search.controller;

import com.example.common.exception.BizCodeEnum;
import com.example.common.to.es.SkuESModel;
import com.example.common.utils.R;
import com.example.gulimall.search.service.MallSearchService;
import com.example.gulimall.search.service.SearchService;
import com.example.gulimall.search.vo.SearchParam;
import com.example.gulimall.search.vo.SearchResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;
    @Autowired
    private MallSearchService mallSearchService;

    /**
     * 商品上架
     * @param skuESModelList
     * @return
     */
    @ResponseBody
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

    @GetMapping(path = "/list.html")
    public String listPage(SearchParam param){
        SearchResult searchResult = mallSearchService.search(param);
        return "list";
    }
}
