package com.hope.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hope.rbac.entity.Role;

import java.util.List;

/**
 * 文件名：IRoleService
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:43
 * 描述：
 */
public interface IRoleService extends IService<Role> {

    void setRoleMenu(Integer roleId, List<Integer> menuIds);

    List<Integer> getRoleMenu(Integer roleId);
}
