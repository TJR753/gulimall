package com.example.gulimall.product.dao;

import com.example.gulimall.product.entity.SpuImagesEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * spu图片
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
@Mapper
public interface SpuImagesDao extends BaseMapper<SpuImagesEntity> {

    void insertBatchIds(List<SpuImagesEntity> imagesEntityList);
}
