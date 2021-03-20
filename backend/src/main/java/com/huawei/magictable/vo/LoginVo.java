/***********************************************************
 * @Description : 登录对象
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 16:33
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginVo {
    private String username;
    private String password;
}
