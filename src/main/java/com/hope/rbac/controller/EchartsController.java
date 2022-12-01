package com.hope.rbac.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.hope.rbac.common.Result;
import com.hope.rbac.entity.User;
import com.hope.rbac.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 文件名：EchartsController
 * 创建者：hope
 * 邮箱：1602774287@qq.com
 * 微信：hope4cc
 * 创建时间：2022/12/1-17:28
 * 描述：
 */
@Slf4j
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private IUserService userService;


    @GetMapping("/members")
    public Result members() {
        List<User> list = userService.list();
        int q1 = 0; // 第一季度
        int q2 = 0; // 第二季度
        int q3 = 0; // 第三季度
        int q4 = 0; // 第四季度
        for (User user : list) {
            Date createTime = user.getCreateTime();
            Quarter quarter = DateUtil.quarterEnum(createTime);
            switch (quarter) {
                case Q1: q1 += 1; break;
                case Q2: q2 += 1; break;
                case Q3: q3 += 1; break;
                case Q4: q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1, q2, q3, q4));
    }
}
