package com.jaygoaler.pitools.controller;

import com.jaygoaler.pitools.service.LoversPrattleService;
import com.jaygoaler.pitools.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/10/30 17:09
 * <p>
 * description: 下载文件操作控制器
 */
@Slf4j
@Api(tags = "情话控制器", value = "情话控制器")
@RestController
@RequestMapping("/prattle")
public class PrattleController extends BaseApiController {

    @Resource
    private LoversPrattleService loversPrattleService;


    @ApiOperation("随机获取")
    @GetMapping("/get")
    public ResponseResult downloadLabgFile() {
        try {
            return loversPrattleService.getRandomPrattle();
        } catch (NullPointerException e) {
            log.error("JjjcSlfgPzController_error", e);
        } catch (Exception e) {
            log.error("JjjcSlfgPzController_error", e);
        }
        return ResponseResult.getFailedInfo("内部服务异常");
    }

}
