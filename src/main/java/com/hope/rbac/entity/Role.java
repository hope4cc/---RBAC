package com.hope.rbac.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 文件名：Role
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:26
 * 描述：
 */
@TableName("sys_role")
@Data
public class Role implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private String name;
    private String description;
    private String flag;
}
