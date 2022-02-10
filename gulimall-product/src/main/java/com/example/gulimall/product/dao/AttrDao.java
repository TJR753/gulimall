package com.example.gulimall.product.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.gulimall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.gulimall.product.vo.AttrEntityVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品属性
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

}
