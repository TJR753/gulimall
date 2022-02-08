package com.example.gulimall.product.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.example.common.valid.AddGroup;
import com.example.common.valid.UpdateGroup;
import com.example.common.valid.UpdateStatusGroup;
import com.example.common.valid.validator.ListValue;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;

/**
 * 品牌
 * 
 * @author tjr
 * @email 1819324794@qq.com
 * @date 2022-02-01 15:31:23
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 品牌id
	 */
	@TableId
	@NotNull(groups = {UpdateGroup.class},message = "更新时，必须指定id")
	private Long brandId;
	/**
	 * 品牌名
	 */
	@NotBlank(groups = {AddGroup.class,UpdateGroup.class},message = "品牌名不能为空")
	private String name;
	/**
	 * 品牌logo地址
	 */
	@NotEmpty(message = "logo不能为空",groups = {AddGroup.class} )
	@URL(message = "必须是一个合法的URL地址",groups = {AddGroup.class,UpdateGroup.class})
	private String logo;
	/**
	 * 介绍
	 */
	private String descript;
	/**
	 * 显示状态[0-不显示；1-显示]
	 */
	@ListValue(vals = {0,1},groups = {AddGroup.class, UpdateStatusGroup.class})
	private Integer showStatus;
	/**
	 * 检索首字母
	 */
	@NotBlank(groups = {AddGroup.class})
	@Pattern(regexp = "^[a-zA-Z]$",message = "必须是合法的单个英文字母",groups = {AddGroup.class,UpdateGroup.class})
	private String firstLetter;
	/**
	 * 排序
	 */
	@Min(value = 0,message = "不能小于0",groups = {AddGroup.class})
	@Max(value = Integer.MAX_VALUE,message = "超过正整数最大范围",groups = {AddGroup.class})
	@NotNull(message = "不能为空",groups = {AddGroup.class})
	private Integer sort;
}
