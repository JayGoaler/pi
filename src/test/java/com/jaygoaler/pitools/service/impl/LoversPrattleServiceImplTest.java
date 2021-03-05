package com.jaygoaler.pitools.service.impl;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("服务测试类")
@SpringBootTest
class LoversPrattleServiceImplTest {

    @Resource
    private RestTemplate restTemplate;

    @DisplayName("随机获取测试")
    @Test
    void getRandomPrattle() {
        String api = "https://chp.shadiao.app/api.php";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(api,String.class);
        if(responseEntity.getStatusCodeValue()!=200){
            System.out.println("异常");
        }
        System.out.println(responseEntity.getBody());

    }
}