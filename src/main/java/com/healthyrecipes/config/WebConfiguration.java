package com.healthyrecipes.config;

import com.healthyrecipes.interceptor.JwtTokenInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;

/**
 * @Author:86198
 * @DATE:2023/12/30 12:06
 * @DESCRIPTION:
 * @VERSION:1.0
 */
@Configuration
@Slf4j
public class WebConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtTokenInterceptor jwtTokenInterceptor;




    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置所有的域名ip都可以
        registry.addMapping("/**").allowedOrigins("*");
        //registry.addMapping("/**").allowedOrigins("http://localhost");

        //开发8080
        //registry.addMapping("/**").allowedOrigins("http://localhost:8080");
    }


    /**
     * 注册自定义拦截器
     *
     * @param registry
     */
    protected void addInterceptors(InterceptorRegistry registry) {
        //自定义需要拦截的内容即可
        log.info("开始注册自定义拦截器...");
        ArrayList<String> path = new ArrayList<>();
        path.add("/user/login");
        path.add("/user/getCode");
        path.add("/user/register");
        path.add("/admin/login");
        path.add("/admin/modifyimg/**");
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/user/**")
                .addPathPatterns("/common/**")
                .addPathPatterns("/test/**")
                .addPathPatterns("/dish/**")
                .addPathPatterns("/food/**")
                .addPathPatterns("/admin/**")
                .excludePathPatterns(path);
    }

    /**
     * 通过knife4j生成接口文档
     * @return
     */
    @Bean
    public Docket docket() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("healthyrecipes接口文档")
                .version("1.0")
                .description("healthyrecipes接口文档")
                .build();
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.healthyrecipes.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 设置静态资源映射
     * @param registry
     */
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
