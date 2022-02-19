package com.example.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.common.constant.ProductConstant;
import com.example.gulimall.product.entity.ProductAttrValueEntity;
import com.example.gulimall.product.service.AttrAttrgroupRelationService;
import com.example.gulimall.product.vo.AttrEntityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.gulimall.product.service.AttrService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;


/**
 * 商品属性
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    ///product/attr/update/{spuId}
    @PostMapping("/update/{spuId}")
    public R updateBySpuId(@PathVariable("spuId")Long spuId,@RequestBody List<ProductAttrValueEntity> list){
        attrService.updateBySpuId(spuId,list);
        return R.ok();
    }

    ///product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public R listForSpu(@PathVariable("spuId")Long spuId){
        List<ProductAttrValueEntity> list= attrService.listForSpu(spuId);
        return R.ok().put("data",list);
    }
    ///product/attr/base/list/{catelogId}
    @GetMapping("/{type}/list/{catelogId}")
    public R baseList(@RequestParam Map<String, Object> params,
                      @PathVariable("catelogId")Long catelogId,
                      @PathVariable("type")String type){
        PageUtils page = attrService.queryPageById(params,catelogId,
                type.equalsIgnoreCase("base")? ProductConstant.Attr.ATTR_BASE.getCode() :ProductConstant.Attr.ATTR_SALE.getCode());

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntityVo attr = attrService.getInfo(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrEntityVo attr){
		attrService.saveVo(attr);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntityVo attr){
		attrService.updateDetail(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
