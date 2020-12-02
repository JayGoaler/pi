package com.jaygoaler.pitools.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jaygoaler.pitools.dto.ApiResultDTO;
import com.jaygoaler.pitools.service.DownloadService;
import com.jaygoaler.pitools.service.FileSystemService;
import com.jaygoaler.pitools.utils.DownloadUtils;
import com.jaygoaler.pitools.utils.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;


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

    @Autowired
    private FileSystemService fileSystemService;

    @Value("${pi.fileSavePath}")
    private String fileSavePath;

    /**
     * 下载指定文件
     *
     * @param fileId
     * @param response
     */
    @Override
    public void downloadLabgFile(String fileId, HttpServletResponse response) throws Exception {
        Map<String,String> pathMap = fileSystemService.getPathMap();
        String path = pathMap.get(fileId);
        // 判断文件是否存在
        if (Files.exists(Paths.get(path))) {
            File file = new File(path);

            if(file.isDirectory()){
                log.warn("文件夹不可下载！");
                DownloadUtils.responseErrorInfo(response, "文件夹不可下载");
            }

            // 文件不存在
            if (!file.exists()) {
                DownloadUtils.responseErrorInfo(response, "ID为：" + fileId + "的文件不存在");
                return;
            }

            try (FileInputStream fileInputStream = new FileInputStream(file);
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
            DownloadUtils.responseErrorInfo(response, "ID为：" + fileId + "的文件不存在");
        }
    }

    @Override
    public ApiResultDTO uploadFile(Map<String,String> map, MultipartFile[] file){
        try{
            String id = map.get("id");
            String path = fileSystemService.getPathMap().get(id);
            if(path==null){
                fileSystemService.getFileTree();
            }
            File dir = new File(path);
            if(!dir.exists()){
                dir.mkdirs();
            }
            if (dir.isFile()){
                path = dir.getParentFile().getAbsolutePath();
            }
            for (MultipartFile multipartFile : file){
                String fileName = multipartFile.getOriginalFilename();
                String diskPath = path+File.separator+fileName;
                File diskFile = new File(diskPath);
                multipartFile.transferTo(diskFile);
            }
        }catch (Exception e) {
            e.printStackTrace();
            ApiResultDTO.getFailedInfo("其他异常，上传失败！！！");
        }
        return ApiResultDTO.getSuccessInfo("文件上传成功！");
    }
}
