package com.example.gulimall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.example.gulimall.product.entity.AttrEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<AttrEntity> listRelation(Long attrGroupId);

    PageUtils listNoRelation(Map<String, Object> params, Long attrGroupId);

    void saveDetail(List<AttrAttrgroupRelationEntity> relationEntityList);

    void deleteDetail(List<AttrAttrgroupRelationEntity> relationEntityList);
}

