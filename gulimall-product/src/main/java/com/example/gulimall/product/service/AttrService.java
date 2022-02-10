package com.example.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.gulimall.product.entity.AttrEntity;
import com.example.gulimall.product.vo.AttrEntityVo;

import java.util.Map;

/**
 * 商品属性
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageById(Map<String, Object> params, Long catelogId, Integer type);

    void saveVo(AttrEntityVo attr);

    AttrEntityVo getInfo(Long attrId);

    void updateDetail(AttrEntityVo attr);
}

