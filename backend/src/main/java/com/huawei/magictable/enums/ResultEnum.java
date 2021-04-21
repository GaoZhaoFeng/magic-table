/***********************************************************
 * @Description : 
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 17:08
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    // 下面是本项目用到的所有错误码
    REGISTER_SUCCESS(0, "注册成功"),
    REGISTER_FAILED(-2, "注册失败,用户已存在"),
    LOGIN_SUCCESS(0, "登录成功"),
    LOGIN_FAILED(-1, "用户名或者密码错误或用户不存在"),
    GET_INFO_SUCCESS(0, "获取用户信息成功"),
    PARAM_ERR(1, "参数不正确"),
    GET_PAGE_DATA_SUCCESS(0, "获取指定条件下的分页数据成功"),
    POST_BOOK_INFO_SUCCESS(0,"根据URL插入书籍成功"),
    GET_BOOK_INFO_SUCCESS(0,"根据URL插入书籍成功");

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;
    private final String message;
}
