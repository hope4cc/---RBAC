package com.hope.rbac.config;

import java.lang.annotation.*;

/**
 * 文件名：AuthAccess
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:38
 * 描述：自定义拦截器  添加在controller方法上
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
}
