package com.jaygoaler.pitools.controller;

import com.jaygoaler.pitools.dto.ApiResultDTO;
import com.jaygoaler.pitools.dto.request.GetLeafRequestDto;
import com.jaygoaler.pitools.service.DownloadService;
import com.jaygoaler.pitools.service.FileSystemService;
import com.jaygoaler.pitools.utils.DownloadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/10/30 17:09
 * <p>
 * description: 文件系统控制器
 */
@Slf4j
@Api(tags = "文件系统控制器", value = "文件系统控制器")
@RestController
@RequestMapping("/fileSystem")
public class FileSystemController extends BaseApiController {

    @Autowired
    private FileSystemService fileSystemService;


    @ApiOperation("获取文件树")
    @GetMapping("/getFileTree")
    public ApiResultDTO getFileTree() {
        try {
            return this.fileSystemService.getFileTree();
        } catch (Exception e) {
            log.error("FileSystemController_error", e);
            return ApiResultDTO.getFailedInfo("内部访问层异常");
        }
    }

    @ApiOperation("获取文件树叶子节点")
    @PostMapping("/getTreeLeaf")
    public ApiResultDTO getTreeLeaf(@RequestBody @Validated GetLeafRequestDto getLeafRequestDto, BindingResult result) {
        String msg = this.checkValidated(result);
        if (StringUtils.isEmpty(msg)) {
            try {
                return this.fileSystemService.getTreeLeafInfo(getLeafRequestDto.getType(),getLeafRequestDto.getId());
            } catch (NullPointerException e) {
                log.error("FileSystemController_error", e);
                return ApiResultDTO.getFailedInfo("内部服务层对象为空");
            } catch (Exception e) {
                log.error("FileSystemController_error", e);
                return ApiResultDTO.getFailedInfo(e.getMessage());
            }
        }
        return ApiResultDTO.getFailedInfo(msg);
    }

}
