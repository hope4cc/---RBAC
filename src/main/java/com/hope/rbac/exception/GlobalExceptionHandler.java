package com.hope.rbac.exception;

import com.hope.rbac.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 文件名：GlobalExceptionHandler
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:35
 * 描述：
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 如果抛出的的是ServiceException，则调用该方法
     *
     * @param se 业务异常
     * @return Result
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result handle(ServiceException se) {
        return Result.error(se.getCode(), se.getMessage());
    }

}
