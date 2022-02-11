package com.example.gulimall.product.service.impl;

import com.example.common.constant.ProductConstant;
import com.example.gulimall.product.dao.AttrAttrgroupRelationDao;
import com.example.gulimall.product.dao.AttrGroupDao;
import com.example.gulimall.product.dao.CategoryDao;
import com.example.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.vo.AttrEntityVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.product.dao.AttrDao;
import com.example.gulimall.product.entity.AttrEntity;
import com.example.gulimall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {
    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;
    @Autowired
    private AttrGroupDao attrGroupDao;
    @Autowired
    private CategoryDao categoryDao;

    @Override
    @Transactional
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public PageUtils queryPageById(Map<String, Object> params, Long catelogId, Integer type) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>().eq("attr_type",type);
        if(catelogId!=0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key=(String)params.get("key");
        if(key!=null){
            wrapper.like("attr_name",key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        //替换分页list
        List<AttrEntityVo> res = pageUtils.getList().stream()
                .map((attrEntity) -> {
                    AttrEntityVo vo = new AttrEntityVo();
                    BeanUtils.copyProperties(attrEntity, vo);
                    //查找关联的groupId
                    if(type== ProductConstant.Attr.ATTR_BASE.getCode()) {
                        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", vo.getAttrId()));
                        if (attrAttrgroupRelationEntity != null) {
                            vo.setGroupName(attrGroupDao.selectById(attrAttrgroupRelationEntity.getAttrGroupId()).getAttrGroupName());
                        }
                    }
                    //查找所属分类
                    StringBuilder s = new StringBuilder();
                    getParent(vo.getCatelogId(), s,null);
                    vo.setCatelogName(s.toString());
                    return vo;
                }).collect(Collectors.toList());
        pageUtils.setList(res);
        return pageUtils;
    }

    private void getParent(Long catelogId,StringBuilder s,List<Long> list){
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);
        if(catelogId!=0){
            getParent(categoryEntity.getParentCid(),s,list);
            if(s!=null){
                s.append(categoryEntity.getName()+"/");
            }
            if(list!=null){
                list.add(catelogId);
            }
        }
    }

    @Override
    @Transactional
    public void saveVo(AttrEntityVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        save(attrEntity);
        //保存关系表
        if(attr.getAttrGroupId()!=null){
            AttrAttrgroupRelationEntity aar = new AttrAttrgroupRelationEntity();
            aar.setAttrGroupId(attr.getAttrGroupId());
            aar.setAttrId(attrEntity.getAttrId());
            //sort为当前同组数量+1
            aar.setAttrSort(attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",aar.getAttrGroupId())));
            attrAttrgroupRelationDao.insert(aar);
        }

    }

    @Override
    @Transactional
    public AttrEntityVo getInfo(Long attrId) {
        AttrEntity attrEntity = getById(attrId);
        AttrEntityVo vo = new AttrEntityVo();
        BeanUtils.copyProperties(attrEntity,vo);
        AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrId));
        if(attrAttrgroupRelationEntity!=null){
            vo.setAttrGroupId(attrAttrgroupRelationEntity.getAttrGroupId());
        }
        ArrayList<Long> list = new ArrayList<>();
        getParent(vo.getCatelogId(), null,list);
        vo.setCatelogPath(list.toArray(new Long[list.size()-1]));
        return vo;
    }


    @Override
    @Transactional
    public void updateDetail(AttrEntityVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        updateById(attrEntity);
        Long attrGroupId = attr.getAttrGroupId();
        if(attrGroupId!=null){
            AttrAttrgroupRelationEntity attrGroupRelationEntity = attrAttrgroupRelationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
            //如果关联存在则更新，否则插入
            if(attrGroupRelationEntity==null){
                attrGroupRelationEntity = new AttrAttrgroupRelationEntity();
                BeanUtils.copyProperties(attr,attrGroupRelationEntity);
                attrGroupRelationEntity.setAttrSort(attrAttrgroupRelationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attrGroupRelationEntity.getAttrId())));
                attrAttrgroupRelationDao.insert(attrGroupRelationEntity);
            }else{
                attrAttrgroupRelationDao.updateById(attrGroupRelationEntity);
            }
        }
    }

}