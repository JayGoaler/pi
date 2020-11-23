package com.jaygoaler.pitools.service;

import javax.servlet.http.HttpServletResponse;

public interface DownloadService {

    /**
     * 下载指定文件
     *
     * @param bm
     * @param response
     */
    void downloadLabgFile(String bm, HttpServletResponse response) throws Exception;

}
