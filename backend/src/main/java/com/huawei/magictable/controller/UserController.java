/***********************************************************
 * @Description : 用户管理接口
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 11:39
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.controller;

import com.huawei.magictable.dao.UserDao;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "User APIs")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserDao userDao;



    @GetMapping("/login")
    @ApiOperation("登录")
    String login() {
        return "Hello World";
    }
}
