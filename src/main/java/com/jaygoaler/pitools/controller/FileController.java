package com.jaygoaler.pitools.controller;

import com.jaygoaler.pitools.dto.ApiResultDTO;
import com.jaygoaler.pitools.service.DownloadService;
import com.jaygoaler.pitools.utils.DownloadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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
@RequestMapping("/file")
public class FileController extends BaseApiController {

    @Autowired
    private DownloadService downloadService;


    @ApiOperation("下载文件")
    @GetMapping("/{fileId}")
    public void downloadLabgFile(@PathVariable String fileId, HttpServletResponse response) {
        try {
            this.downloadService.downloadLabgFile(fileId, response);
        } catch (NullPointerException e) {
            log.error("JjjcSlfgPzController_error", e);
            DownloadUtils.responseErrorInfo(response, "内部服务层对象为空");
        } catch (Exception e) {
            log.error("JjjcSlfgPzController_error", e);
            DownloadUtils.responseErrorInfo(response, e.getMessage());
        }
    }

    @ApiOperation("保存文件")
    @PostMapping("/uploadFile")
    public ApiResultDTO uploadFile(@RequestParam Map<String,String> map, @RequestParam("file") MultipartFile[] file, HttpServletRequest request){
        try {
            return this.downloadService.uploadFile(map,file);
        } catch (NullPointerException e) {
            log.error("JjjcSlfgPzController_error", e);
            return ApiResultDTO.getFailedInfo(e.getMessage());
        }
    }
}
