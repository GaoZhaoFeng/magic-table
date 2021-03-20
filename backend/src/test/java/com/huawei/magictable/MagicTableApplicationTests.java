package com.huawei.magictable;

import com.huawei.magictable.dao.UserDao;
import com.huawei.magictable.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class MagicTableApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    void addUser() {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("123456");
        user.setNickname("管理员");
        user.setDesc("超级管理员");
        user.setAvatar("https://i.gtimg.cn/club/item/face/img/2/15922_100.gif");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userDao.save(user);
        System.out.println(user);
    }
}

