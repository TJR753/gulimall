package com.example.gulimall.product.web.controller;

import com.alibaba.fastjson.JSON;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.service.CategoryService;
import com.example.gulimall.product.vo.Catalog2JsonVO;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(path = {"/","/index","/index.html"})
    public String index(Model model){
        //查出一级分类
        List<CategoryEntity> list=categoryService.getLevelOne();

        model.addAttribute("categoryList",list);
        return "index";
    }

    //index/catalog.json
    @ResponseBody
    @GetMapping(path = "/index/catalog.json")
    public Map<String,List<Catalog2JsonVO>> getCatalogJson(){
        //渲染二级三级分类
        return categoryService.getCatalogJson();
    }

}
