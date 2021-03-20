/***********************************************************
 * @Description : 用户注册对象
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 16:30
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.vo;

import lombok.Data;

@Data
public class RegisterVo {
    String username;
    String password;
    String nickname;
    String email;
    String phone;
    String avatar;
    String desc = "";
}
