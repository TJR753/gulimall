package com.example.gulimall.ware.dao;

import com.example.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品库存
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 17:11:05
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {
	
}
