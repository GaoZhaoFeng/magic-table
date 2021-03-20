/***********************************************************
 * @Description : 
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 16:08
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.config;

import com.huawei.magictable.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截指定的namespace下的api
        registry.addInterceptor(requestInterceptor).addPathPatterns("/user/**").addPathPatterns("/exam/**");
    }

}
