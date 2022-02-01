package com.example.gulimall.order.dao;

import com.example.gulimall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 16:48:41
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
