package com.example.gulimall.product.feign;

import com.example.common.to.es.SkuESModel;
import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("gulimall-search")
public interface SearchFeignService {
    @PostMapping(path = "/es/search/up/save")
    R up(@RequestBody List<SkuESModel> skuESModelList);
}
