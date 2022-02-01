package com.example.gulimall.order.dao;

import com.example.gulimall.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 16:48:41
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
