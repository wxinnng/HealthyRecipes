package com.healthyrecipes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableCaching//开启缓存注解功能
public class HealthyRecipesApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthyRecipesApplication.class, args);
    }

    /*注入RestTemplate*/
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
