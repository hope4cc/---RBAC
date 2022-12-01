package com.hope.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文件名：RoleMenu
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:26
 * 描述：
 */
@TableName("sys_role_menu")
@Data
public class RoleMenu {

    private Integer roleId;
    private Integer menuId;

}

