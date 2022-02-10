package com.example.gulimall.product.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.gulimall.product.entity.AttrAttrgroupRelationEntity;
import com.example.gulimall.product.entity.AttrEntity;
import com.example.gulimall.product.entity.CategoryEntity;
import com.example.gulimall.product.service.AttrAttrgroupRelationService;
import com.example.gulimall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.example.gulimall.product.entity.AttrGroupEntity;
import com.example.gulimall.product.service.AttrGroupService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 属性分组
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;
    /**
     * 列表
     */
    @RequestMapping("/list/{catId}")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catId")Long catId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page=attrGroupService.queryPageById(params,catId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        ArrayList<Long> list = new ArrayList<>();
        getParentId(attrGroup.getCatelogId(),list);
        attrGroup.setCatelogPath(list.toArray(new Long[list.size()-1]));
        return R.ok().put("attrGroup", attrGroup);
    }
    private void getParentId(Long catelogId,List<Long> list){
        CategoryEntity category = categoryService.getById(catelogId);
        if(category.getParentCid()!=0){
            getParentId(category.getParentCid(),list);
        }
        list.add(category.getCatId());
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateByDetail(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

//    http://localhost:88/api/product/attrgroup/1/attr/relation?t=1644482021067

    /**
     * 根据分组id查找关联的属性
     * @param attrGroupId 分组id
     * @return
     */
    @RequestMapping("/{attrGroupId}/attr/relation")
    public R listAttr(@PathVariable("attrGroupId")Long attrGroupId){
        List<AttrEntity> list = attrAttrgroupRelationService.listRelation(attrGroupId);
        return R.ok().put("data",list);
    }

    /**
     * 返回没有与该分组关联的数据
     * @param params
     * @return
     */
    @GetMapping("/{attrGroupId}/noattr/relation")
    public R pageNoAttr(@RequestParam Map<String,Object> params,@PathVariable("attrGroupId")Long attrGroupId){
        PageUtils page = attrAttrgroupRelationService.listNoRelation(params, attrGroupId);
        return R.ok().put("page",page);
    }
    @PostMapping(path="/attr/relation")
    public R addRelation(@RequestBody List<AttrAttrgroupRelationEntity> relationEntityList){
        attrAttrgroupRelationService.saveDetail(relationEntityList);
        return R.ok();
    }
    @PostMapping(path="/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrAttrgroupRelationEntity> relationEntityList){
        attrAttrgroupRelationService.deleteDetail(relationEntityList);
        return R.ok();
    }
}
