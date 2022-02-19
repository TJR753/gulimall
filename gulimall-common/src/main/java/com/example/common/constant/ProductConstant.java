package com.example.common.constant;

public class ProductConstant {
    public enum Attr{
//        属性类型[0-销售属性，1-基本属性]
        ATTR_SALE(0,"销售属性"),ATTR_BASE(1,"基本属性");
        int code;
        String msg;
        Attr(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
    public enum Status{
        //      上架状态[0 - 下架，1 - 上架]
        PRODUCT_NEW(0,"下架"),PRODUCT_UP(1,"上架");
        int code;
        String msg;
        Status(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
