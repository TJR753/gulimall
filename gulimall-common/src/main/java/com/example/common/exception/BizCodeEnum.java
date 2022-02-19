package com.example.common.exception;

/**
 * 10:通用
 *      000: 系统未知异常
 *      001：参数格式校验
 * 11：商品
 *      000：商家异常
 * 12：订单
 * 13：购物车
 * 14.物流
 */
public enum BizCodeEnum {
    UNKNOW_EXCEPTION(10000,"系统未知异常"),
    PRODUCT_UP_EXCEPTION(11000,"上架异常"),
    VALID_EXCEPTION(10001,"参数校验异常");
    Integer code;
    String msg;
    BizCodeEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
