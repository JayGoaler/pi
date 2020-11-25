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
    public ApiResultDTO uploadFile(HttpServletRequest request){
        try{
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //设置工厂的缓冲区的大小，当上传的文件大小超过缓冲区的大小时，就会生成一个临时文件存放到指定的临时目录当中。
            //设置缓冲区的大小为100KB，如果不指定，那么缓冲区的大小默认是10KB
            factory.setSizeThreshold(1024*100);
            /*//设置上传时生成的临时文件的保存目录
            factory.setRepository(tmpFile);*/
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //监听文件上传进度
            upload.setProgressListener((pBytesRead, pContentLength, arg2) -> System.out.println("文件大小为：" + pContentLength + ",当前已处理：" + pBytesRead));
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if(!ServletFileUpload.isMultipartContent(request)){
                //按照传统方式获取数据

            }

            //设置上传单个文件的大小的最大值，目前是设置为1024*1024字节，也就是1MB
            upload.setFileSizeMax(1024*1024*10);
            //设置上传文件总量的最大值，最大值=同时上传的多个文件的大小的最大值的和，目前设置为10MB
            upload.setSizeMax(1024*1024*30);
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for(FileItem item : list){
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    System.out.println(name + "=" + value);
                }else{//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    String filename = item.getName();
                    System.out.println(filename);
                    if(filename==null || filename.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\")+1);
                    //得到上传文件的扩展名
                    String fileExtName = filename.substring(filename.lastIndexOf(".")+1);
                    filename = filename.substring(0,filename.lastIndexOf("."));
                    //如果需要限制上传的文件类型，那么可以通过文件的扩展名来判断上传的文件类型是否合法
                    System.out.println("上传的文件的扩展名是："+fileExtName);
                    //保存文件

                }
            }
        }catch (FileUploadBase.FileSizeLimitExceededException e) {
            e.printStackTrace();
            ApiResultDTO.getFailedInfo("单个文件超出最大值！！！");
        }catch (FileUploadBase.SizeLimitExceededException e) {
            e.printStackTrace();
            ApiResultDTO.getFailedInfo("上传文件的总的大小超出限制的最大值！！！");
        }catch (Exception e) {
            e.printStackTrace();
            ApiResultDTO.getFailedInfo("其他异常，上传失败！！！");
        }
        return ApiResultDTO.getSuccessInfo("文件上传成功！");
    }
}
