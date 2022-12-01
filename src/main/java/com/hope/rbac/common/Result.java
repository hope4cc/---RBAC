package com.hope.rbac.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件名：Result
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:29
 * 描述：接口统一返回包装类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String msg;
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "", data);
    }

    public static Result error(String code, String msg) {
        return new Result(code, msg, null);
    }

    public static Result error() {
        return new Result(Constants.CODE_500, "系统错误", null);
    }

}
