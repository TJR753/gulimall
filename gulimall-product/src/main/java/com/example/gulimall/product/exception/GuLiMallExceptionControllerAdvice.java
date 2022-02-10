package com.example.gulimall.product.exception;

import com.example.common.exception.BizCodeEnum;
import com.example.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GuLiMallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleInvalidException(MethodArgumentNotValidException e){
        log.error("错误原因："+e.getCause()+"\n"+"错误类型："+e.getClass()+"\n"+"错误信息："+e.getMessage());
        HashMap<String, Object> msg = new HashMap<>();
        BindingResult result = e.getBindingResult();
        result.getFieldErrors().forEach((fieldError)->{
            msg.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(),BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data",msg);
    }
    @ExceptionHandler
    public R handleException(Exception e){
        log.error("错误原因："+e.getCause()+"\n"+"错误类型："+e.getClass()+"\n"+"错误信息："+e.getMessage()
        +"\n"+"错误位置："+e.getStackTrace()[0].getLineNumber()+":"+e.getStackTrace()[0].getMethodName());
        log.error(e.toString());
        return R.error(BizCodeEnum.UNKNOW_EXCEPTION.getCode(),BizCodeEnum.UNKNOW_EXCEPTION.getMsg());
    }
}
