package com.jaygoaler.pitools.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jaygoaler.pitools.dto.ApiResultDTO;
import com.jaygoaler.pitools.model.*;
import com.jaygoaler.pitools.service.ChunkService;
import com.jaygoaler.pitools.service.DownloadService;
import com.jaygoaler.pitools.service.FileInfoService;
import com.jaygoaler.pitools.utils.DownloadUtils;
import com.jaygoaler.pitools.utils.FileInfoUtils;
import com.jaygoaler.pitools.utils.ServletUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

    @Value("${pi.fileSavePath}")
    private String uploadFolder;

    private final DownloadService downloadService;
    private final FileInfoService fileInfoService;
    private final ChunkService chunkService;

    @Autowired
    public FileController(DownloadService downloadService, FileInfoService fileInfoService, ChunkService chunkService) {
        this.downloadService = downloadService;
        this.fileInfoService = fileInfoService;
        this.chunkService = chunkService;
    }

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

    /**
     * 上传文件块
     * @param chunk
     * @return
     */
    @PostMapping("/chunk")
    public String uploadChunk(TChunkInfo chunk) {
        String apiRlt = "200";

        MultipartFile file = chunk.getUpfile();
        log.info("file originName: {}, chunkNumber: {}", file.getOriginalFilename(), chunk.getChunkNumber());

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(FileInfoUtils.generatePath(uploadFolder, chunk));
            //文件写入指定路径
            Files.write(path, bytes);
            if(chunkService.saveChunk(chunk) < 0) {
                apiRlt = "415";
            }

        } catch (IOException e) {
            e.printStackTrace();
            apiRlt = "415";
        }
        return apiRlt;
    }

    @GetMapping("/chunk")
    public UploadResult checkChunk(TChunkInfo chunk, HttpServletResponse response) {
        UploadResult ur = new UploadResult();

        //默认返回其他状态码，前端不进去checkChunkUploadedByResponse函数，正常走标准上传
        response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);

        String file = uploadFolder + "/" + chunk.getIdentifier() + "/" + chunk.getFilename();

        //先判断整个文件是否已经上传过了，如果是，则告诉前端跳过上传，实现秒传
        if(FileInfoUtils.fileExists(file)) {
            ur.setSkipUpload(true);
            ur.setLocation(file);
            response.setStatus(HttpServletResponse.SC_OK);
            ur.setMessage("完整文件已存在，直接跳过上传，实现秒传");
            return ur;
        }

        //如果完整文件不存在，则去数据库判断当前哪些文件块已经上传过了，把结果告诉前端，跳过这些文件块的上传，实现断点续传
        ArrayList<Integer> list = chunkService.checkChunk(chunk);
        if (list !=null && list.size() > 0) {
            ur.setSkipUpload(false);
            ur.setUploadedChunks(list);
            response.setStatus(HttpServletResponse.SC_OK);
            ur.setMessage("部分文件块已存在，继续上传剩余文件块，实现断点续传");
            return ur;
        }
        return ur;
    }

    @PostMapping("/mergeFile")
    public String mergeFile(@RequestBody TFileInfoVO fileInfoVO){

        String rlt = "FALURE";

        //前端组件参数转换为model对象
        TFileInfo fileInfo = new TFileInfo();
        fileInfo.setFilename(fileInfoVO.getName());
        fileInfo.setIdentifier(fileInfoVO.getUniqueIdentifier());
        fileInfo.setId(fileInfoVO.getId());
        fileInfo.setTotalSize(fileInfoVO.getSize());
        fileInfo.setRefProjectId(fileInfoVO.getRefProjectId());

        //进行文件的合并操作
        String filename = fileInfo.getFilename();
        String file = uploadFolder + "/" + fileInfo.getIdentifier() + "/" + filename;
        String folder = uploadFolder + "/" + fileInfo.getIdentifier();
        String fileSuccess = FileInfoUtils.merge(file, folder, filename);

        fileInfo.setLocation(file);

        //文件合并成功后，保存记录至数据库
        if("200".equals(fileSuccess)) {
            if(fileInfoService.addFileInfo(fileInfo) > 0) {
                rlt = "SUCCESS";
            }
        }

        //如果已经存在，则判断是否同一个项目，同一个项目的不用新增记录，否则新增
        if("300".equals(fileSuccess)) {
            List<TFileInfo> tfList = fileInfoService.selectFileByParams(fileInfo);
            if(tfList != null) {
                if(tfList.size() == 0 || (tfList.size() > 0 && !fileInfo.getRefProjectId().equals(tfList.get(0).getRefProjectId()))) {
                    if(fileInfoService.addFileInfo(fileInfo) > 0) {
                        rlt = "SUCCESS";
                    }
                }
            }
        }

        return rlt;
    }

    /**
     * 查询列表
     *
     * @return ApiResult
     */
    @RequestMapping(value = "/selectFileList", method = RequestMethod.POST)
    public ApiResult selectFileList(@RequestBody QueryInfo query){
        PageHelper.startPage(query.getPageIndex(), query.getPageSize());
        List<TFileInfo> list =  fileInfoService.selectFileList(query);
        PageInfo<TFileInfo> pageResult = new PageInfo<>(list);
        return ApiResult.success(pageResult);
    }

    /**
     * 下载文件
     * @param req
     * @param resp
     */
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(HttpServletRequest req, HttpServletResponse resp){
        String location = req.getParameter("location");
        String fileName = req.getParameter("filename");
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        OutputStream fos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(location));
            fos = resp.getOutputStream();
            bos = new BufferedOutputStream(fos);
            ServletUtils.setFileDownloadHeader(req, resp, fileName);
            int byteRead = 0;
            byte[] buffer = new byte[8192];
            while ((byteRead = bis.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, byteRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();
                bis.close();
                fos.close();
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public ApiResult deleteFile(@RequestBody TFileInfo tFileInfo ){
        int result = fileInfoService.deleteFile(tFileInfo);
        return ApiResult.success(result);
    }
}
