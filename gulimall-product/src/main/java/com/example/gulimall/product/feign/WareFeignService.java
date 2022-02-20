package com.example.gulimall.product.feign;

import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("gulimall-ware")
public interface WareFeignService {
    @GetMapping("ware/waresku/has/stock")
    R hasStock(@RequestParam Long skuId);
}
