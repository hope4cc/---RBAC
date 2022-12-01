package com.hope.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hope.rbac.entity.Menu;

import java.util.List;

/**
 * 文件名：IMenuService
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:44
 * 描述：
 */
public interface IMenuService extends IService<Menu> {

    List<Menu> findMenus(String name);
}
