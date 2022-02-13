package com.example.gulimall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.gulimall.ware.vo.DoneVo;
import com.example.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.gulimall.ware.entity.PurchaseEntity;
import com.example.gulimall.ware.service.PurchaseService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 采购信息
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 17:11:05
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    //TODO 完成采购
    @PostMapping("/done")
    public R purchaseDone(@RequestBody DoneVo doneVo){
        purchaseService.purchaseDone(doneVo);
        return R.ok();
    }

    /**
     * 合并采购单
     * @param mergeVo
     * @return
     */
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.merge(mergeVo);
        return R.ok();
    }

    /**
     * 领取采购单
     * @param ids 采购单id
     * @return
     */
    @PostMapping("/receive")
    public R receive(@RequestBody List<Long> ids){
        purchaseService.receive(ids);
        return R.ok();
    }

    @RequestMapping("/unreceive/list")
    public R unreceiveList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.saveDetail(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
