package com.example.gulimall.product.service.impl;

import com.example.gulimall.product.dao.SpuImagesDao;
import com.example.gulimall.product.dao.SpuInfoDescDao;
import com.example.gulimall.product.entity.SpuImagesEntity;
import com.example.gulimall.product.entity.SpuInfoDescEntity;
import com.example.gulimall.product.service.SpuImagesService;
import com.example.gulimall.product.vo.SpuSaveVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.product.dao.SpuInfoDao;
import com.example.gulimall.product.entity.SpuInfoEntity;
import com.example.gulimall.product.service.SpuInfoService;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private SpuImagesService spuImagesService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(SpuSaveVo spuSaveVo) {
        //1.保存spuInfo
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        save(spuInfoEntity);
        //2.保存spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",spuSaveVo.getDecript()));
        spuInfoService.save(spuInfoEntity);
        //3.保存spu_images
        List<String> images = spuSaveVo.getImages();
        List<SpuImagesEntity> imagesEntityList = images.stream().map((image) -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(image);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(imagesEntityList);
        //4.保存sku_info

        //5.保存sku_images
        //6.保存product_attr_value
    }

}