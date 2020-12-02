package com.jaygoaler.pitools.service;

import com.jaygoaler.pitools.dto.ApiResultDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author JayGoal
 */
public interface DownloadService {

    /**
     * 下载指定文件
     *
     * @param bm
     * @param response
     */
    void downloadLabgFile(String bm, HttpServletResponse response) throws Exception;

    ApiResultDTO uploadFile(Map<String,String> map, MultipartFile[] file);
}
