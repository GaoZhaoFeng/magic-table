/***********************************************************
 * @Description : 用户服务
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 16:29
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.service;

import com.huawei.magictable.model.User;
import com.huawei.magictable.vo.LoginVo;
import com.huawei.magictable.vo.RegisterVo;

public interface UserService {
    /**
     * 注册
     *
     * @param registerVo 注册参数
     * @return 注册成功后的用户信息
     */
    User register(RegisterVo registerVo);

    /**
     * 登录接口，登录成功返回token
     *
     * @param loginVo 登录参数
     * @return 成功返回token，失败返回null
     */
    String login(LoginVo loginVo);

    /**
     * 根据用户id获取用户信息
     *
     * @param id 用户在mongo中的id
     * @return 用户实体
     */
    User getInfo(String id);
}
