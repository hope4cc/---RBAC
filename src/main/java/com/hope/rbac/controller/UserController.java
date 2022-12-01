package com.hope.rbac.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hope.rbac.common.Constants;
import com.hope.rbac.common.Result;
import com.hope.rbac.dto.UserDTO;
import com.hope.rbac.entity.User;
import com.hope.rbac.service.IUserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 文件名：UserController
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:51
 * 描述：
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        //获取用户名和密码
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
//        是否为 null
//        是否为 “”
//        是否为空字符串(引号中间有空格) 如： " "。
//        制表符、换行符、换页符和回车
//        StringUtils的isBlank()方法可以一次性校验这三种情况，返回值都是true,否则为false
        if (StrUtil.isBlank(username) || StrUtil.isBlank(password)) {
            return Result.error(Constants.CODE_400, "参数错误");
        }
        UserDTO dto = userService.login(userDTO);
        return Result.success(dto);
    }

    /**
     * 根据用户名查询当前用户，登录后进行查询
     * @param username
     * @return
     */
    @GetMapping("/username/{username}")
    public Result findByUsername(@PathVariable String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return Result.success(userService.getOne(queryWrapper));
    }

    /**
     * 分页
     * @param pageNum
     * @param pageSize
     * @param username
     * @param email
     * @param address
     * @return
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String username,
                           @RequestParam(defaultValue = "") String email,
                           @RequestParam(defaultValue = "") String address) {

        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!Strings.isEmpty(username),"username", username);
        queryWrapper.like(!Strings.isEmpty(email),"email",email);
        queryWrapper.like(!Strings.isEmpty(address),"address", address);
        queryWrapper.orderByDesc("id");
        return Result.success(userService.page(page, queryWrapper));
    }

}
