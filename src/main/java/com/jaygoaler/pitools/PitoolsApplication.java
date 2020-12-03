package com.jaygoaler.pitools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

/**
 * @author JayGoal
 */
@SpringBootApplication
public class PitoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PitoolsApplication.class, args);
    }
    @Bean
    public RestTemplate createRestTemplate(){
        return new RestTemplate();
    }
}
