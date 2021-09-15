package com.jaygoaler.pitools.service;

import com.jaygoaler.pitools.model.QueryInfo;
import com.jaygoaler.pitools.model.TFileInfo;

import java.util.List;

public interface FileInfoService {
	
	int addFileInfo(TFileInfo fileInfo);
	
	List<TFileInfo> selectFileByParams(TFileInfo fileInfo);
	
	 /**
     * 查询
     *
     * @param query 查询条件
     * @return List
     */
    List<TFileInfo> selectFileList(QueryInfo query);
                    
    
    /**
     * 删除
     * @param tFileInfo
     * @return
     */
    int deleteFile(TFileInfo tFileInfo);
}
