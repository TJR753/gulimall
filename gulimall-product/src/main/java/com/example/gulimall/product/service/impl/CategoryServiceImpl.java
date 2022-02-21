package com.example.gulimall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.gulimall.product.entity.CategoryBrandRelationEntity;
import com.example.gulimall.product.service.CategoryBrandRelationService;
import com.example.gulimall.product.vo.Catalog2JsonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.product.dao.CategoryDao;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listMenu() {
        List<CategoryEntity> entities = baseMapper.selectList(null);
        return entities.stream()
                .filter((categoryEntity) -> {
                    return categoryEntity.getParentCid() == 0;
                })
                .map((categoryEntity) -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, entities));
                    return categoryEntity;
                }).sorted(Comparator.comparingInt(categoryEntity -> (categoryEntity.getSort() == null ? 0 : categoryEntity.getSort())))
                .collect(Collectors.toList());
    }

    /**
     * 递归查找子类商品
     * @param root 当前商品
     * @param entities 全部商品
     * @return
     */
    private List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity> entities){
        return entities.stream()
                .filter((categoryEntity) -> {
                    return root.getCatId() == categoryEntity.getParentCid();
                })
                .map((categoryEntity) -> {
                    categoryEntity.setChildren(getChildren(categoryEntity, entities));
                    return categoryEntity;
                }).sorted(Comparator.comparingInt(categoryEntity -> (categoryEntity.getSort() == null ? 0 : categoryEntity.getSort())))
                .collect(Collectors.toList());
    }

    @Override
    public int removeMenuByIds(List<Long> asList) {
        //TODO 检查被引用的菜单项，无法被删除
        return baseMapper.deleteBatchIds(asList);
    }

    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogName(category.getName());
        categoryBrandRelationService.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id",category.getCatId()));
    }

    @Override
    public List<CategoryEntity> getLevelOne() {
        List<CategoryEntity> list = list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return list;
    }

    @Override
    public Map<String, List<Catalog2JsonVO>> getCatalogJson() {
        //查出所有数据
        List<CategoryEntity> list = list();
        List<CategoryEntity> levelOne = getParentCid(list, 0L);
        Map<String, List<Catalog2JsonVO>> collect = levelOne.stream().collect(Collectors.toMap(category1Entity -> category1Entity.getCatId().toString(), category1Entity -> {
            //根据一级分类找所有二级分类
            List<CategoryEntity> category2EntityList = getParentCid(list,category1Entity.getParentCid());
            //二级vo
            List<Catalog2JsonVO> catalog2JsonVOList = category2EntityList.stream().map(category2Entity -> {
                Catalog2JsonVO catalog2JsonVO = new Catalog2JsonVO(category2Entity.getParentCid().toString(), null, category2Entity.getCatId().toString(), category2Entity.getName());
                //根据二级分类找所有三级分类
                List<CategoryEntity> category3EntityList = getParentCid(list,category2Entity.getParentCid());
                //一级vo
                List<Catalog2JsonVO.Catalog3JsonVO> catalog3JsonVOList = category3EntityList.stream().map(category3Entity -> {
                    Catalog2JsonVO.Catalog3JsonVO catalog3JsonVO = new Catalog2JsonVO.Catalog3JsonVO(category2Entity.getCatId().toString(), category3Entity.getCatId().toString(), category3Entity.getName());
                    return catalog3JsonVO;
                }).collect(Collectors.toList());
                catalog2JsonVO.setCatalog3List(catalog3JsonVOList);
                return catalog2JsonVO;
            }).collect(Collectors.toList());
            return catalog2JsonVOList;
        }));
        return collect;
    }
    private List<CategoryEntity> getParentCid(List<CategoryEntity> list,Long parentCid){
        return list.stream().filter(item -> item.getParentCid() == parentCid).collect(Collectors.toList());
    }
}