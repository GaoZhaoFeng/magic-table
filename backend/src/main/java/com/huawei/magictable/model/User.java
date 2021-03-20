/***********************************************************
 * @Description : 用户类
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 12:55
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    @Id
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String desc;
    private String avatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}
