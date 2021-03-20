/***********************************************************
 * @Description : 
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 16:42
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.service.impl;

import com.huawei.magictable.dao.UserDao;
import com.huawei.magictable.model.User;
import com.huawei.magictable.service.UserService;
import com.huawei.magictable.util.JwtUtils;
import com.huawei.magictable.vo.LoginVo;
import com.huawei.magictable.vo.RegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao userDao;

    @Override
    public User register(RegisterVo registerVo) {
        List<User> userList = userDao.findByUsername(registerVo.getUsername());
        // 用户已经存在
        if (userList != null && userList.size() > 0) {
            return null;
        }
        User user = new User();
        // 好像还缺少个用户名,用"exam_user_手机号"来注册：需要校验唯一性数据字段已经设置unique了，失败会异常地
        user.setUsername(registerVo.getUsername());
        // 初始化昵称和用户名相同
        user.setNickname(registerVo.getNickname());
        // Todo：这里还需要进行加密处理，后续解密用Base64.decode()
        user.setPassword(registerVo.getPassword());
        // 设置头像图片地址, 用户在前端上传，转换成base64存储在这个字段
        user.setAvatar(registerVo.getAvatar());
        // 设置描述信息，随便设置段默认的
        user.setDesc(registerVo.getDesc());
        // Todo：需要验证这个邮箱是不是已经存在
        user.setEmail(registerVo.getEmail());
        // Todo：需要验证手机号是否已经存在
        user.setPhone(registerVo.getPhone());
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setLastLoginTime(new Date());
        userDao.save(user);
        System.out.println(user);
        return user;
    }

    @Override
    public String login(LoginVo loginVo) {
        List<User> userList = userDao.findByUsername(loginVo.getUsername());
        if (userList != null && userList.size() > 0) {
            User user = userList.get(0);
            if (loginVo.getPassword().equals(user.getPassword())) {
                // 更新最近一次登录的时间戳
                user.setLastLoginTime(new Date());
                userDao.save(user);
                // 如果密码相等地话说明认证成功,返回生成的token，有效期为一天
                return JwtUtils.genJsonWebToken(user);
            }
        }
        return null;
    }

    @Override
    public User getInfo(String id) {
        return userDao.findById(id).orElse(null);
    }
}
