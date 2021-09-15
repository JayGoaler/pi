package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.mapper.TFileInfoMapper;
import com.jaygoaler.pitools.model.QueryInfo;
import com.jaygoaler.pitools.model.TFileInfo;
import com.jaygoaler.pitools.service.FileInfoService;
import com.jaygoaler.pitools.utils.SnowflakeIdWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文件处理类
 * @author 洋葱骑士
 *
 */
@Service
public class FileInfoServiceImpl implements FileInfoService {

	@Resource
	TFileInfoMapper tFileInfoMapper;
	
    @Override
    public int addFileInfo(TFileInfo fileInfo) {
    	fileInfo.setId(SnowflakeIdWorker.getUUID()+SnowflakeIdWorker.getUUID());
        return tFileInfoMapper.insertSelective(fileInfo);
    }
    
    @Override
    public List<TFileInfo> selectFileByParams(TFileInfo fileInfo) {
        return tFileInfoMapper.selectFileByParams(fileInfo);
    }
    
    @Override
	public List<TFileInfo> selectFileList(QueryInfo query) {
		return tFileInfoMapper.selectFileList(query);
	}

	@Override
	public int deleteFile(TFileInfo tFileInfo) {
		TFileInfo t = new TFileInfo();
		t.setId(tFileInfo.getId());
		t.setDelFlag("1");
		return tFileInfoMapper.updateByPrimaryKeySelective(t);
	}
}
