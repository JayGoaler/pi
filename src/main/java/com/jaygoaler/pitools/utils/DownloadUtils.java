package com.jaygoaler.pitools.utils;

import com.alibaba.fastjson.JSONObject;
import com.jaygoaler.pitools.dto.ApiResultDTO;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class DownloadUtils {

    /**
     * 返回下载错误信息
     *
     * @param response
     * @param message
     */
    public static void responseErrorInfo(HttpServletResponse response, String message) {
        ApiResultDTO msg = ApiResultDTO.getFailedInfo(message);
        ServletOutputStream outputStream = null;
        try {
            response.setHeader("content-type", "application/json");
            outputStream = response.getOutputStream();
            outputStream.write(JSONObject.toJSONString(msg).getBytes());
            outputStream.flush();
        } catch (IOException e) {
            log.error("error", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
    }

    /**
     * 返回下载错误信息
     *
     * @param response
     * @param fileName
     * @param fileData
     */
    public static void responseFileStream(HttpServletResponse response, String fileName, byte[] fileData) {
        ServletOutputStream outputStream = null;
        try {
            response.setHeader("content-type", "application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName.substring(fileName.lastIndexOf(File.separator)+1), "UTF-8"));
            response.addHeader("Content-Length", "" + fileData.length);
            outputStream = response.getOutputStream();
            outputStream.write(fileData);
            outputStream.flush();
        } catch (IOException e) {
            log.error("error", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("error", e);
                }
            }
        }
    }

}
