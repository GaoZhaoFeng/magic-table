/***********************************************************
 * @Description : 用户表的数据库操作类
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 12:59
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.dao;

import com.huawei.magictable.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserDao extends MongoRepository<User, String> {
    List<User> findByUsername(String username);
}
