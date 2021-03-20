/***********************************************************
 * @Description : 接口文档配置
 * @author      : 梁山广(Liang Shan Guang)
 * @date        : 2021/3/20 15:36
 * @email       : liangshanguang2@gmail.com
 ***********************************************************/
package com.huawei.magictable.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api() {

        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        ticketPar.name("accessToken").description("Rest接口权限认证token,无需鉴权可为空")
                .modelRef(new ModelRef("string")).parameterType("header")
                //header中的ticket参数非必填，传空也可以
                .required(false).build();
        // 根据每个方法名也知道当前方法在设置什么参数
        pars.add(ticketPar.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                // 自行修改为自己的包路径
                .apis(RequestHandlerSelectors.basePackage("com.huawei"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("magic table by springboot2&vue3")
                .description("在线表格系统 by 梁山广 at 2021")
                .termsOfServiceUrl("https://gitee.com/lsgwr/magic-table")
                .version("1.0。0")
                .contact(new Contact("liangshanguang", "https://github.com/19920625lsg", "liangshanguang2@gmail.com"))
                .build();
    }
}
