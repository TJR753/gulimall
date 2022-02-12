package com.example.common.constant;

public class WareConstant {
    public enum PurchaseStatusEnum {
        CREATED(0,"创建"),ASSIGNED(1,"已分配"),
        PROGRESSED(2,"正在采购"),FINISHED(3,"已完成"),
        FAILED(4,"采购失败");
        int code;
        String msg;
        PurchaseStatusEnum(int code, String msg){
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
