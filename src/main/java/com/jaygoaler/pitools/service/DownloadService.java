package com.jaygoaler.pitools.service;

import com.jaygoaler.pitools.dto.ApiResultDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    ApiResultDTO uploadFile(HttpServletRequest request);
}
