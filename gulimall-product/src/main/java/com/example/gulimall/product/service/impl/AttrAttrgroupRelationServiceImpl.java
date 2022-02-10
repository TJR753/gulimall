package com.example.gulimall.product.service.impl;



import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.common.utils.Query;
import com.example.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.example.gulimall.product.dao.AttrDao;
import com.example.gulimall.product.dao.AttrGroupDao;
import com.example.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.example.gulimall.product.entity.AttrEntity;
import com.example.gulimall.product.service.AttrAttrgroupRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Autowired
    private AttrDao attrDao;
    @Autowired
    private AttrGroupDao attrGroupDao;

    @Override
    @Transactional
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public List<AttrEntity> listRelation(Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> list = list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<AttrEntity> res = list.stream().map((entity) -> {
            return attrDao.selectOne(new QueryWrapper<AttrEntity>().eq("attr_id", entity.getAttrId()));
        }).collect(Collectors.toList());
        return res;
    }

    @Override
    @Transactional
    public PageUtils listNoRelation(Map<String, Object> params, Long attrGroupId) {
        List<AttrAttrgroupRelationEntity> listRelation = list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrGroupId));
        List<Long> relationIds = listRelation.stream().map((entity) -> {
            return entity.getAttrId();
        }).collect(Collectors.toList());
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        if(key!=null){
            wrapper.like("attr_name",key);
        }
        IPage<AttrEntity> page = attrDao.selectPage(
                new Query<AttrEntity>().getPage(params),
                wrapper.and((obj)->{obj.notIn("attr_id", relationIds);}));
        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(List<AttrAttrgroupRelationEntity> relationEntityList) {
        relationEntityList.stream().forEach((relationEntity -> {
            Integer sort = attrGroupDao.selectById(relationEntity.getAttrGroupId()).getSort();
            relationEntity.setAttrSort(sort);
            save(relationEntity);
        }));
    }

    @Override
    @Transactional
    public void deleteDetail(List<AttrAttrgroupRelationEntity> relationEntityList) {
        relationEntityList.stream().forEach((relationEntity -> {
            remove(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id",relationEntity.getAttrId())
                    .eq("attr_group_id",relationEntity.getAttrGroupId()));
        }));
    }
}