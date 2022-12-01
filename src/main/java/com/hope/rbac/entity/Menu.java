package com.hope.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * 文件名：Menu
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:27
 * 描述：
 */
@Data
@TableName("sys_menu")
public class Menu {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String path;
    private String icon;
    private String description;
    @TableField(exist = false)//mybatis-plus忽略字段
    private List<Menu> children;
    private Integer pid;
    private String pagePath;
    private String sortNum;

}
