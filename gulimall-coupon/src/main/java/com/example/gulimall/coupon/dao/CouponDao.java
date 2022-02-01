package com.example.gulimall.coupon.dao;

import com.example.gulimall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 17:05:45
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
