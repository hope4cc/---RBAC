package com.hope.rbac.exception;

import lombok.Data;

/**
 * 文件名：ServiceException
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:35
 * 描述：
 */
@Data
public class ServiceException  extends RuntimeException {
    private String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }

}
