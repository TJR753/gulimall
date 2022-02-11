package com.example.gulimall.product.service.impl;

import com.example.gulimall.product.dao.BrandDao;
import com.example.gulimall.product.dao.CategoryDao;
import com.example.gulimall.product.entity.BrandEntity;
import com.example.gulimall.product.service.BrandService;
import com.example.gulimall.product.service.CategoryService;
import com.example.gulimall.product.vo.BrandEntityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.product.dao.CategoryBrandRelationDao;
import com.example.gulimall.product.entity.CategoryBrandRelationEntity;
import com.example.gulimall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private BrandDao brandDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id",(String)params.get("brandId"))
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        String name = brandDao.selectById(brandId).getName();
        categoryBrandRelation.setBrandName(name);
        String name1 = categoryDao.selectById(catelogId).getName();
        categoryBrandRelation.setCatelogName(name1);
        this.save(categoryBrandRelation);
    }

    @Override
    public List<BrandEntityVo> getByCatelogId(Long catId) {
        List<CategoryBrandRelationEntity> list = list(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntityVo> brandEntityVoList = list.stream().map((entity) -> {
            BrandEntity brandEntity = brandDao.selectById(entity.getBrandId());
            BrandEntityVo vo = new BrandEntityVo();
            vo.setBrandName(brandEntity.getName());
            vo.setBrandId(brandEntity.getBrandId());
            return vo;
        }).collect(Collectors.toList());
        return brandEntityVoList;
    }
}