package com.huawei.magictable.service;


import com.alibaba.fastjson.JSONObject;

public interface BookService {
    /**
     * 根据url获取书籍信息存入
     *
     * @param url 注册参数
     * @return 注册成功后的用户信息
     */
    JSONObject insertBookByURl(String url);

    JSONObject insertBooksByURl(String url);

    JSONObject queryAllBooks();
}
