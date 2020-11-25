package com.jaygoaler.pitools.service.impl;

import com.google.common.collect.Maps;
import com.jaygoaler.pitools.dto.ApiResultDTO;
import com.jaygoaler.pitools.service.FileSystemService;
import com.jaygoaler.pitools.utils.Encrypt;
import com.jaygoaler.pitools.vo.FileInfo;
import com.jaygoaler.pitools.vo.FileSimpleInfo;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: pi
 * @description: 文件系统服务类
 * @author: JayGoal
 * @create: 2020-11-25 10:10
 **/
@Service
@Slf4j
public class FileSystemServiceImpl implements FileSystemService {

    @Value("${pi.fileSavePath}")
    private String fileSavePath;

    private Map<String,String> pathMap;

    @Override
    public ApiResultDTO getFileTree() throws FileNotFoundException {
        List<FileInfo> resultList = createFileTree(fileSavePath,null);
        if(pathMap==null){
            pathMap = Maps.newHashMap();
        }
        pathMap = resultList.stream().collect(Collectors.toMap(FileInfo::getId,FileInfo::getPath));
        List<FileSimpleInfo> simpleInfos = Lists.newArrayList();
        resultList.forEach(fileInfo -> {
            simpleInfos.add(new FileSimpleInfo(fileInfo.getId(),fileInfo.getName(),fileInfo.getParentId()));
        });
        return ApiResultDTO.getSuccessInfo(simpleInfos);
    }

    @Override
    public Map<String, String> getPathMap() {
        if(pathMap==null){
            pathMap = Maps.newHashMap();
        }
        return pathMap;
    }

    private List<FileInfo> createFileTree(String filepath, String parentid) throws FileNotFoundException {
        List<FileInfo> list = Lists.newArrayList();
        File file = new File(filepath);
        //1.判断文件
        if(!file.exists()){
            throw new FileNotFoundException("文件不存在");
        }
        String name = file.getName();
        String path = file.getAbsolutePath();
        FileInfo tree = new FileInfo(Encrypt.getMD5Code(path).toLowerCase(),name,path,parentid);
        list.add(tree);
        //3.获取文件夹路径下面的所有文件递归调用；
        if(file.isDirectory()){
            String[] fileList = file.list();
            assert fileList != null;
            for (String s : fileList) {
                //根据当前文件夹，拼接其下文文件形成新的路径
                String newFilePath = path + File.separator + s;
                list.addAll(createFileTree(newFilePath, tree.getId()));
            }
        }
        return list;
    }
}
