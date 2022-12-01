package com.hope.rbac.dto;

import com.hope.rbac.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * 文件名：UserDTO
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-15:42
 * 描述：
 */
@Data
public class UserDTO {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String avatarUrl;
    private String token;
    private String role;
    private List<Menu> menus;
}
