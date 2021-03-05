package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.service.LoversPrattleService;
import com.jaygoaler.pitools.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @program: pi
 * @description: 情话服务
 * @author: JayGoal
 * @create: 2021-03-05 16:02
 **/
@Slf4j
@Service
public class LoversPrattleServiceImpl implements LoversPrattleService {

    @Resource
    private RestTemplate restTemplate;

    @Override
    public ResponseResult getRandomPrattle() {
        int successCode = 200;
        String api = "https://chp.shadiao.app/api.php";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(api,String.class);
        if(responseEntity.getStatusCodeValue()!=successCode){
            return ResponseResult.getSuccessInfo("","哟，你来啦？可惜接口连不上了~~");
        }
        return ResponseResult.getSuccessInfo("",responseEntity.getBody());
    }
}
