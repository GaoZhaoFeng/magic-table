/***********************************************************
 * @Description : 用户类
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 12:55
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "user")
public class User {
    @Id
    private String id;
    private String username;
    /**
     * 密码不要返回，防止泄露
     */
    @JsonIgnore
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String desc;
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastLoginTime;
}
