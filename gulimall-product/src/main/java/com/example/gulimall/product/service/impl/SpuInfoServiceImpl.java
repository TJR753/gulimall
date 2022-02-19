package com.example.gulimall.product.service.impl;

import com.example.common.constant.ProductConstant;
import com.example.common.to.MemberPrice;
import com.example.common.to.SkuReductionTo;
import com.example.common.to.SpuBoundTo;
import com.example.common.to.es.SkuESModel;
import com.example.common.utils.R;
import com.example.gulimall.product.entity.*;
import com.example.gulimall.product.feign.CouponFeignService;
import com.example.gulimall.product.feign.SearchFeignService;
import com.example.gulimall.product.feign.WareFeignService;
import com.example.gulimall.product.service.*;
import com.example.gulimall.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
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

import com.example.gulimall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoService spuInfoService;
    @Autowired
    private SpuImagesService spuImagesService;
    @Autowired
    private AttrService attrService;
    @Autowired
    private ProductAttrValueService productAttrValueService;
    @Autowired
    private SkuInfoService skuInfoService;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private CouponFeignService couponFeignService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private WareFeignService wareFeignService;
    @Autowired
    private SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void saveDetail(SpuSaveVo spuSaveVo) {
        //1.保存spuInfo
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuSaveVo,spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        save(spuInfoEntity);
        //2.保存spu_info_desc
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",spuSaveVo.getDecript()));
        spuInfoDescService.save(spuInfoDescEntity);
        //3.保存spu_images
        List<String> images = spuSaveVo.getImages();
        List<SpuImagesEntity> imagesEntityList = images.stream().map((image) -> {
            SpuImagesEntity spuImagesEntity = new SpuImagesEntity();
            spuImagesEntity.setSpuId(spuInfoEntity.getId());
            spuImagesEntity.setImgUrl(image);
            return spuImagesEntity;
        }).collect(Collectors.toList());
        spuImagesService.saveBatch(imagesEntityList);
        //4.保存规格参数,product_attr_value
        List<BaseAttr> baseAttrs = spuSaveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrs.stream().map(baseAttr -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrValue(baseAttr.getAttrValues());
            productAttrValueEntity.setAttrId(baseAttr.getAttrId());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            productAttrValueEntity.setQuickShow(baseAttr.getShowDesc());
            AttrEntity attr = attrService.getById(baseAttr.getAttrId());
            productAttrValueEntity.setAttrName(attr.getAttrName());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntityList);
        //5.保存积分信息

        //6.保存sku_info
        List<Skus> skus = spuSaveVo.getSkus();
        if(skus!=null&&skus.size()>0){
            skus.stream().forEach(sku -> {
                List<Image> imageList = sku.getImages();
                String defaultImage = "";
                for (Image image : imageList) {
                    if (image.getDefaultImg() == 1) {
                        defaultImage = image.getImgUrl();
                        break;
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(sku, skuInfoEntity);
//            "skuName": "Apple XR 黑色 6GB",
//            "price": "1999",
//            "skuTitle": "Apple XR 黑色 6GB",
//            "skuSubtitle": "Apple XR 黑色 6GB",
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setSkuDefaultImg(defaultImage);
                skuInfoService.save(skuInfoEntity);
                //5.保存sku_images
                List<SkuImagesEntity> skuImagesEntityList = imageList.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    BeanUtils.copyProperties(image, skuImagesEntity);
                    skuImagesEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuImagesEntity;
                }).filter(skuImagesEntity -> {
                    if(skuImagesEntity.getImgUrl()==null){
                        return false;
                    }
                    return true;
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntityList);
                //6.保存product_attr_value
                List<Attr> attrs = sku.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntities = attrs.stream().map(attr -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuInfoEntity.getSkuId());
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntities);
                //保存sku_ladder和sku_full_reduction
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(sku,skuReductionTo);
                List<MemberPrice> memberPrice = sku.getMemberPrice();
                skuReductionTo.setMemberPrice(memberPrice);
                skuReductionTo.setSkuId(skuInfoEntity.getSkuId());
                couponFeignService.saveInfo(skuReductionTo);
            });

        }
        //7.远程调用，保存spu_bound
        Bounds bounds = spuSaveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        BeanUtils.copyProperties(bounds,spuBoundTo);
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        couponFeignService.save(spuBoundTo);

    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String)params.get("key");
        if(StringUtils.isNotEmpty(key)){
            wrapper.and((w)->{
                w.eq("id",key).or().like("spu_name",key);
            });
        }
        String status = (String)params.get("status");
        if(StringUtils.isNotEmpty(status)){
            wrapper.eq("publish_status",status);
        }
        String brandId = (String)params.get("brandId");
        if(StringUtils.isNotEmpty(brandId)&&!"0".equals(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String catelogId = (String)params.get("catelogId");
        if(StringUtils.isNotEmpty(catelogId)&&!"0".equals(brandId)){
            wrapper.eq("catalog_id",catelogId);
        }
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        //spuId查skuId
        List<SkuInfoEntity> skuInfoEntityList = skuInfoService.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        //spuId查attrs,是否需要检索[0-不需要，1-需要]
        List<ProductAttrValueEntity> list = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));
        List<SkuESModel.Attrs> attrsList = list.stream().map(productAttrValueEntity -> {
            SkuESModel.Attrs attrs = new SkuESModel.Attrs();
            BeanUtils.copyProperties(productAttrValueEntity, attrs);
            return attrs;
        }).filter(attrs -> {
            AttrEntity attrEntity = attrService.getById(attrs.getAttrId());
            return attrEntity.getSearchType()==1;
        }).collect(Collectors.toList());
        List<SkuESModel> skuESModelList = skuInfoEntityList.stream().map((skuInfoEntity -> {
            SkuESModel skuESModel = new SkuESModel();
            skuESModel.setSkuId(skuInfoEntity.getSkuId());
            skuESModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
            skuESModel.setSkuPrice(skuInfoEntity.getPrice());
            skuESModel.setSkuTitle(skuInfoEntity.getSkuTitle());
            //查库存 hasStock
            Boolean hasStock=(Boolean)wareFeignService.hasStock(skuInfoEntity.getSkuId()).get("hasStock");
            skuESModel.setHasStock(hasStock);
            //TODO 计算热度评分 hotScore 0
            skuESModel.setHotScore(0L);
            //1.spuId查catalogId，分类信息
            SpuInfoEntity spuInfo = getById(spuId);
            skuESModel.setSpuId(spuId);
            skuESModel.setBrandId(spuInfo.getBrandId());
            skuESModel.setCatalogId(spuInfo.getCatalogId());
            //一级分类名字
            CategoryEntity category = categoryService.getById(spuInfo.getCatalogId());
            skuESModel.setCatalogName(category.getName());
            //2.spuId查brandId，品牌信息
            BrandEntity brand = brandService.getById(spuInfo.getBrandId());
            skuESModel.setBrandImg(brand.getLogo());
            skuESModel.setBrandName(brand.getName());
            skuESModel.setAttrs(attrsList);
            return skuESModel;
        })).collect(Collectors.toList());
        // 发送给es进行保存
        R up = searchFeignService.up(skuESModelList);
        if((Integer)up.get("code")==0){
            //远程调用成功,修改商品状态
            spuInfoService.updateStatusById(spuId, ProductConstant.Status.PRODUCT_UP.getCode());
        }else{
            //远程调用失败
            //TODO 重复调用，接口幂等性问题，重试机制
        }
    }

    @Override
    public void updateStatusById(Long spuId, int productUp) {
        baseMapper.updateStatusById(spuId,productUp);
    }

}