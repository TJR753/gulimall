package com.example.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.gulimall.product.entity.CategoryBrandRelationEntity;
import com.example.gulimall.product.redis.RedisKey;
import com.example.gulimall.product.service.CategoryBrandRelationService;
import com.example.gulimall.product.vo.Catalog2JsonVO;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedissonClient redissonClient;

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
    @CacheEvict(value ={"category"},allEntries = true)
    public int removeMenuByIds(List<Long> asList) {
        //TODO 检查被引用的菜单项，无法被删除
        return baseMapper.deleteBatchIds(asList);
    }

    @Override
    @CacheEvict(value ={"category"},allEntries = true)
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setCatelogName(category.getName());
        categoryBrandRelationService.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id",category.getCatId()));
    }

    @Override
    @Cacheable(value = {"category"},key="#root.methodName")
    public List<CategoryEntity> getLevelOne(){
        List<CategoryEntity> list = list(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return list;
    }

//    @Cacheable(value = {"category"},key = "#root.methodName")
    public List<CategoryEntity> getAllCatalogJson(){
        String json = stringRedisTemplate.opsForValue().get(RedisKey.CATALOG_JSON_KEY);
        if(StringUtils.isEmpty(json)){
            RLock lock = redissonClient.getLock("lock");
            try{
                lock.lock();
                if(StringUtils.isEmpty(json)){
                    return list();
                }
            }finally {
                lock.unlock();
            }
        }
        return JSON.parseObject(json,new TypeReference<List<CategoryEntity>>(){});
    }

    @Deprecated
    private List<CategoryEntity> getAllCatalogJsonRedisLock(){
        String json = stringRedisTemplate.opsForValue().get(RedisKey.CATALOG_JSON_KEY);
        if(StringUtils.isEmpty(json)){
            //缓存为空，从数据库中取
            List<CategoryEntity> list=null;
            //设置过期时间和获得锁
            String uuid=UUID.randomUUID().toString();
            //1.刚获得锁，断电，设置过期时间
            //2.自己的锁过期，删除别人的锁
            System.out.println("获取分布式锁");
            Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock",uuid , 300, TimeUnit.SECONDS);
            if(Boolean.TRUE.equals(lock)){
                //从数据库获取数据
                try{
                    System.out.println("从数据库中查数据");
                    list=list();
                    stringRedisTemplate.opsForValue().set(RedisKey.CATALOG_JSON_KEY,JSON.toJSONString(list));
                }finally {
                    String script="if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                            "then\n" +
                            "    return redis.call(\"del\",KEYS[1])\n" +
                            "else\n" +
                            "    return 0\n" +
                            "end";
                    stringRedisTemplate.execute(new DefaultRedisScript<Long>(script),Arrays.asList("lock"),uuid);
                }
            }else{
                //自旋
                System.out.println("获取分布式锁失败，等待重试。。。");
                return getAllCatalogJsonRedisLock();
            }

        }
        return JSON.parseObject(json,new TypeReference<List<CategoryEntity>>(){});
    }

    @Deprecated
    private List<CategoryEntity> getAllCatalogJsonLocalLock(){
        String json = stringRedisTemplate.opsForValue().get(RedisKey.CATALOG_JSON_KEY);
        if(StringUtils.isEmpty(json)){
            //缓存为空，从数据库中取
            List<CategoryEntity> list=null;
            synchronized (this){
                //在查一遍缓存
                json=stringRedisTemplate.opsForValue().get(RedisKey.CATALOG_JSON_KEY);
                if(StringUtils.isEmpty(json)){
                    list = list();
                    stringRedisTemplate.opsForValue().set(RedisKey.CATALOG_JSON_KEY,JSON.toJSONString(list));
                    System.out.println("查数据库");
                    return list;
                }
            }
        }
        return JSON.parseObject(json,new TypeReference<List<CategoryEntity>>(){});
    }

    @Override
    @Cacheable(value = {"category"},key = "#root.methodName")
    public Map<String, List<Catalog2JsonVO>> getCatalogJson() {
        //查出所有数据
        List<CategoryEntity> list = getAllCatalogJson();
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