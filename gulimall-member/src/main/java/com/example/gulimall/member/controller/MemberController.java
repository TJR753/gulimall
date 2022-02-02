package com.example.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.gulimall.member.feign.CouponFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import com.example.gulimall.member.entity.MemberEntity;
import com.example.gulimall.member.service.MemberService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;

import javax.servlet.http.HttpServletResponse;


/**
 * 会员
 *
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 17:02:44
 */
@RestController
@RequestMapping("member/member")
@RefreshScope
public class MemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponFeignService couponFeignService;

    @GetMapping("/student")
    public R student(HttpServletResponse response){
//        response.addHeader("access-control-allow-origin", "*");
        return R.ok().put("id",1);
    }
    @GetMapping("/course")
    public R course(HttpServletResponse response){
//        response.addHeader("access-control-allow-origin", "*");
        return R.ok().put("subject","math").put("id",1);
    }
    @GetMapping("/score")
    public R list(HttpServletResponse response){
//        response.addHeader("access-control-allow-origin", "*");
        return R.ok().put("score",88);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
