package com.jaygoaler.pitools.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: pitools
 * @description: swagger2配置
 * @author: JayGoal
 * @create: 2020-09-25 15:56
 **/
@EnableOpenApi
@Configuration
public class Swagger2Config {
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Pi Tools API")
                .version("1.0")
                .build();
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jaygoaler.pitools"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(apiKeyList())
                .enable(true);
    }

    /**
     *授权
     * @return
     */
    private List<SecurityScheme> apiKeyList() {
        List<SecurityScheme> apiKeyList = new ArrayList<>();
        apiKeyList.add(new ApiKey("LOGIN", "Authorization", "header"));
        return apiKeyList;
    }
}
