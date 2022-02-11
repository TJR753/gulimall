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
import com.example.gulimall.product.entity.AttrGroupEntity;
import com.example.gulimall.product.service.AttrAttrgroupRelationService;
import com.example.gulimall.product.vo.AttrRelationEntityVo;
import com.example.gulimall.product.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        //只能关联规格参数
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type",1);
        //模糊查询
        String key = (String)params.get("key");
        if(key!=null){
            wrapper.and((obj->{
                obj.like("attr_name",key).or().like("attr_id",key);
            }));
        }
        List<AttrAttrgroupRelationEntity> relationList = list();
        List<Long> relationIds = relationList.stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());
        wrapper.and((obj)->{
            obj.notIn("attr_id",relationIds);
        });
        IPage<AttrEntity> attrEntityIPage = attrDao.selectPage(
                new Query<AttrEntity>().getPage(params), wrapper
        );
        return new PageUtils(attrEntityIPage);
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

    @Override
    @Transactional
    public List<AttrRelationEntityVo> getRelationByCatelogId(Long catId) {
        List<AttrGroupEntity> attrGroupEntityList = attrGroupDao.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catId));
        //查出分类id下的分组id
        return attrGroupEntityList.stream().map((entity) -> {
            AttrRelationEntityVo vo = new AttrRelationEntityVo();
            BeanUtils.copyProperties(entity, vo);
            //根据分组id查出规格参数，销售属性
            List<AttrAttrgroupRelationEntity> relationEntityList = list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", entity.getAttrGroupId()));
            List<AttrVo> attrVoList = relationEntityList.stream().map((relationEntity) -> {
                AttrEntity attrEntity = attrDao.selectById(relationEntity.getAttrId());
                AttrVo attrVo = new AttrVo();
                BeanUtils.copyProperties(attrEntity, attrVo);
                return attrVo;
            }).collect(Collectors.toList());
            vo.setAttrs(attrVoList);
            return vo;
        }).collect(Collectors.toList());
    }
}