package com.maple.server;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * swagger bean
 *
 * @author maple
 * @date 2019/7/9 15:38
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        //设置文档类型
        return new Docket(DocumentationType.SWAGGER_2)
                //api的相关描述信息，通常显示在页面的最上方
                .apiInfo(apiInfo())
                .select()
                //设置扫描的包
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                //设置扫描哪些controller，这里设置扫描全部，可以传入正则表达式
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("基础权限系统")
                .description("基础权限系统API")
                .build();
    }


    //    @Override
    //    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //        registry.addResourceHandler("/**")
    //                .addResourceLocations("classpath:/static/");
    //        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
    //                "classpath:/META-INF/resources/");
    //        registry.addResourceHandler("doc.html").addResourceLocations(
    //                "classpath:/META-INF/resources/");
    //        registry.addResourceHandler("/webjars/**")
    //                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    //    }

}
