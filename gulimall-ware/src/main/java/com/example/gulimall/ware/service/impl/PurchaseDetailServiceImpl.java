package com.example.gulimall.ware.service.impl;

import com.example.common.constant.WareConstant;
import com.example.common.utils.R;
import com.example.gulimall.ware.entity.PurchaseEntity;
import com.example.gulimall.ware.feign.ProductFeignService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.gulimall.ware.dao.PurchaseDetailDao;
import com.example.gulimall.ware.entity.PurchaseDetailEntity;
import com.example.gulimall.ware.service.PurchaseDetailService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Autowired
    private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(StringUtils.isNotEmpty(key)){
            wrapper.and((w)->{
                w.eq("sku_id",key);
            });
        }
        String status = (String) params.get("status");
        if(StringUtils.isNotEmpty(status)){
            wrapper.eq("status",status);
        }
        String wareId = (String) params.get("wareId");
        if(StringUtils.isNotEmpty(wareId)){
            wrapper.eq("ware_id",wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                new QueryWrapper<PurchaseDetailEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(PurchaseDetailEntity purchaseDetail) {
        Long skuId = purchaseDetail.getSkuId();
        R info = productFeignService.infoPrice(skuId);
        BigDecimal price = new BigDecimal((Double) info.get("price"));
        purchaseDetail.setSkuPrice(price);
        save(purchaseDetail);
    }

    @Override
    @Transactional
    public void updateBatchByPurchaseId(List<PurchaseEntity> purchaseEntityList) {
        purchaseEntityList.stream().forEach(purchaseEntity -> {
            List<PurchaseDetailEntity> purchaseDetailEntities = this.getBaseMapper().selectList(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", purchaseEntity.getId()));
            List<PurchaseDetailEntity> list = purchaseDetailEntities.stream().map(purchaseDetailEntity -> {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseStatusEnum.PROGRESSED.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            this.updateBatchById(list);
        });
    }

}