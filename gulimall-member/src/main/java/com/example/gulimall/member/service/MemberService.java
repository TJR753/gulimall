package com.example.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 17:02:44
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

