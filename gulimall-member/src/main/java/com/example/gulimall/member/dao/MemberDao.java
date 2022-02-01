package com.example.gulimall.member.dao;

import com.example.gulimall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 17:02:44
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
