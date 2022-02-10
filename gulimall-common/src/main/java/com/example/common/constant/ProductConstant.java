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
}
