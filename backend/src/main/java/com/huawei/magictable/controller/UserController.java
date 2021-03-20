/***********************************************************
 * @Description : 用户管理接口
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 11:39
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.controller;

import com.huawei.magictable.enums.ResultEnum;
import com.huawei.magictable.model.User;
import com.huawei.magictable.service.UserService;
import com.huawei.magictable.vo.LoginVo;
import com.huawei.magictable.vo.RegisterVo;
import com.huawei.magictable.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "User APIs")
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    @ApiOperation("注册")
    ResultVO<User> register(@RequestBody RegisterVo registerVo) {
        ResultVO<User> resultVO;
        // 注册信息的完善，还有唯一性校验没(用户名、邮箱和手机号)已经在user表中通过unique来设置了
        User user = userService.register(registerVo);
        if (user != null) {
            // 注册成功
            resultVO = new ResultVO<>(ResultEnum.REGISTER_SUCCESS.getCode(), ResultEnum.REGISTER_SUCCESS.getMessage(), user);
        } else {
            // 注册失败，如果不是出异常地话，一般是因为用户已经存在
            resultVO = new ResultVO<>(ResultEnum.REGISTER_FAILED.getCode(), ResultEnum.REGISTER_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    ResultVO<String> login(@RequestBody LoginVo loginVo) {
        // 用户登录
        ResultVO<String> resultVO;
        String token = userService.login(loginVo);
        if (token != null) {
            // 登录成功
            resultVO = new ResultVO<>(ResultEnum.LOGIN_SUCCESS.getCode(), ResultEnum.LOGIN_SUCCESS.getMessage(), token);
        } else {
            // 登录失败
            resultVO = new ResultVO<>(ResultEnum.LOGIN_FAILED.getCode(), ResultEnum.LOGIN_FAILED.getMessage(), null);
        }
        return resultVO;
    }

    @GetMapping("/userInfo")
    @ApiOperation("获取用户信息")
    ResultVO<User> getUserInfo(HttpServletRequest request) {
        String userId = (String) request.getAttribute("user_id");
        User user = userService.getInfo(userId);
        return new ResultVO<>(ResultEnum.GET_INFO_SUCCESS.getCode(), ResultEnum.GET_INFO_SUCCESS.getMessage(), user);
    }

    @GetMapping("/test_token")
    @ApiOperation("测试接口")
    String test(HttpServletRequest request) {
        // 下面这两个属性都是登录拦截器从token中解析地，当用户名不对或者token过期时是走不到接口内的
        String userId = (String) request.getAttribute("user_id");
        String username = (String) request.getAttribute("username");
        System.out.println("用户id：" + userId);
        System.out.println("用户名：" + username);
        return "用户id：" + userId + "\n用户名：" + username;
    }
}
