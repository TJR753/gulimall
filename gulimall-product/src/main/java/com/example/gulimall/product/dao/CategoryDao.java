package com.example.gulimall.product.dao;

import com.example.gulimall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
