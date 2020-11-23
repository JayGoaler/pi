package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.service.DownloadService;
import com.jaygoaler.pitools.utils.DownloadUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/10/30 17:22
 * <p>
 * description: 下载文件服务
 */
@Slf4j
@Service
public class DownloadServiceImpl implements DownloadService {

    /**
     * 下载指定文件
     *
     * @param bm
     * @param response
     */
    @Override
    public void downloadLabgFile(String bm, HttpServletResponse response) throws Exception {
        String path = ""+bm;
        // 判断文件是否存在
        if (Files.exists(Paths.get(path))) {
            File labgFile = new File(path);

            // 文件不存在
            if (!labgFile.exists()) {
                DownloadUtils.responseErrorInfo(response, "类案报告编码为：" + bm + "的文件不存在");
            }

            try (FileInputStream fileInputStream = new FileInputStream(labgFile);
                 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                byte[] readByte = new byte[1024 * 1024];
                while ((fileInputStream.read(readByte)) != -1) {
                    byteArrayOutputStream.write(readByte, 0, readByte.length);
                    byteArrayOutputStream.flush();
                }
                DownloadUtils.responseFileStream(response, path, byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                log.error("error", e);
                DownloadUtils.responseErrorInfo(response, "下载文件出现错误：" + e.getMessage());
            }
        } else {
            DownloadUtils.responseErrorInfo(response, "编码为：" + bm + "的文件不存在");
        }
    }
}
