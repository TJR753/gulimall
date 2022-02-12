package com.example.gulimall.ware.service.impl;

import com.example.common.constant.WareConstant;
import com.example.gulimall.ware.entity.PurchaseDetailEntity;
import com.example.gulimall.ware.service.PurchaseDetailService;
import com.example.gulimall.ware.vo.MergeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.ware.dao.PurchaseDao;
import com.example.gulimall.ware.entity.PurchaseEntity;
import com.example.gulimall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void merge(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if(purchaseId==null){
            //创建新的订单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setPriority(0);
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }
        //TODO 确认采购单状态在进行合并
        //合并订单
        List<Long> items = mergeVo.getItems();
        Long a=purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = purchaseDetailService.getBaseMapper().selectBatchIds(items);
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailEntityList.stream().map(item -> {
            item.setPurchaseId(finalPurchaseId);
            item.setStatus(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
            return item;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);

    }

    @Override
    public void saveDetail(PurchaseEntity purchase) {
        purchase.setCreateTime(new Date());
        save(purchase);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
                        .eq("status", WareConstant.PurchaseStatusEnum.CREATED.getCode())
                        .or().eq("status", WareConstant.PurchaseStatusEnum.ASSIGNED.getCode())
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void receive(List<Long> ids) {
        //更新所有采购单status,time
        List<PurchaseEntity> purchaseEntityList = ids.stream().map(id -> {
            PurchaseEntity purchaseEntity = this.getById(id);
            return purchaseEntity;
        }).filter(purchaseEntity -> {
            //只更新statuswei 0/1的
            return purchaseEntity.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()
                    || purchaseEntity.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
        }).map(purchaseEntity -> {
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.PROGRESSED.getCode());
            purchaseEntity.setUpdateTime(new Date());
            return purchaseEntity;
        }).collect(Collectors.toList());
        this.updateBatchById(purchaseEntityList);
        //更新采购单里的采购项
        purchaseDetailService.updateBatchByPurchaseId(purchaseEntityList);
    }

}