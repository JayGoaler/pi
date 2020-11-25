package com.jaygoaler.pitools.service;

import com.jaygoaler.pitools.dto.ApiResultDTO;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * @author JayGoal
 */
public interface FileSystemService {

    /**
     * 获取文件树
     * @return
     * @throws FileNotFoundException
     */
    ApiResultDTO getFileTree() throws FileNotFoundException;

    /**
     * 获取缓存的集合
     * @return
     */
    Map<String,String> getPathMap();
}
