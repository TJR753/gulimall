package com.example.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.vo.Catalog2JsonVO;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listMenu();

    /**
     * 批量删除分类维护中的菜单项
     * @param asList
     * @return
     */
    int removeMenuByIds(List<Long> asList);

    void updateDetail(CategoryEntity category);

    List<CategoryEntity> getLevelOne();

    Map<String, List<Catalog2JsonVO>> getCatalogJson();
}

