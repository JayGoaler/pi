package com.jaygoaler.pitools.controller;

import com.jaygoaler.pitools.service.DownloadService;
import com.jaygoaler.pitools.utils.DownloadUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/10/30 17:09
 * <p>
 * description: 下载文件操作控制器
 */
@Slf4j
@Api(tags = "下载文件操作控制器", value = "下载文件操作控制器")
@RestController
@RequestMapping("/download")
public class DownloadController extends BaseApiController {

    @Autowired
    private DownloadService downloadService;

    @GetMapping("/{labgbm}")
    public void downloadLabgFile(@PathVariable String bm, HttpServletResponse response) {
        try {
            this.downloadService.downloadLabgFile(bm, response);
        } catch (NullPointerException e) {
            log.error("JjjcSlfgPzController_error", e);
            DownloadUtils.responseErrorInfo(response, "内部服务层对象为空");
        } catch (Exception e) {
            log.error("JjjcSlfgPzController_error", e);
            DownloadUtils.responseErrorInfo(response, e.getMessage());
        }
    }
}
