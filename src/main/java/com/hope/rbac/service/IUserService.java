package com.hope.rbac.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hope.rbac.dto.UserDTO;
import com.hope.rbac.entity.User;

/**
 * 文件名：IUserService
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:42
 * 描述：
 */
public interface IUserService extends IService<User> {

    /**
     * 登录接口
     * @param userDTO
     * @return
     */
    UserDTO login(UserDTO userDTO);



}
