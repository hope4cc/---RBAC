package com.hope.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件名：Dict
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-17:33
 * 描述：element ui图标 存入数据库 新增菜单时填充
 */
@Data
@TableName(value="sys_dict")
public class Dict implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;

    private String value;

    private String type;
}
