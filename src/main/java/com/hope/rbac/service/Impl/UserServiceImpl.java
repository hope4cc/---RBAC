package com.hope.rbac.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hope.rbac.common.Constants;
import com.hope.rbac.dto.UserDTO;
import com.hope.rbac.entity.Menu;
import com.hope.rbac.entity.User;
import com.hope.rbac.exception.ServiceException;
import com.hope.rbac.mapper.RoleMapper;
import com.hope.rbac.mapper.RoleMenuMapper;
import com.hope.rbac.mapper.UserMapper;
import com.hope.rbac.service.IMenuService;
import com.hope.rbac.service.IUserService;
import com.hope.rbac.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件名：UserServiceImpl
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:44
 * 描述：
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    private static final Log LOG = Log.get();

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private IMenuService menuService;

    @Override
    public UserDTO login(UserDTO userDTO) {
        // 对用户密码 md5加密
        userDTO.setPassword(SecureUtil.md5(userDTO.getPassword()));
        //调用getUserInfo方法 根据账号密码 从数据库中查询用户信息
        User one = getUserInfo(userDTO);
        if (one != null) {
            //如果不为空，数据库存在该用户
            //复制Bean对象属性 第一个参数是原对象 //one 数据库中的用户信息
            //               第二个参数是需要数据的对象 userDTO 客户端的用户
            BeanUtil.copyProperties(one, userDTO, true);
            // 设置token
            // 用户的id作为载荷
            // 用户密码 作为 token 的密钥
            //Json web token 包含三部分，头部、数据、签名
            String token = TokenUtils.getToken(one.getId().toString(), one.getPassword());
            //将生产的token set 会用户对象
            userDTO.setToken(token);
            //先获取角色 找到关联关系
            String role = one.getRole(); // ROLE_ADMIN
            // 设置用户的菜单列表
            // 调用getRoleMenus获取当前角色的菜单列表
            List<Menu> roleMenus = getRoleMenus(role);
            userDTO.setMenus(roleMenus);
            return userDTO;
        } else {
            throw new ServiceException(Constants.CODE_400, "用户名或密码错误");
        }
    }

    private User getUserInfo(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        queryWrapper.eq("password", userDTO.getPassword());
        User one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
