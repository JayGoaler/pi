package com.jaygoaler.pitools.mapper;


import com.jaygoaler.pitools.model.QueryInfo;
import com.jaygoaler.pitools.model.TFileInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TFileInfoMapper {
	
    int deleteByPrimaryKey(String id);

    int insert(TFileInfo record);

    int insertSelective(TFileInfo record);

    TFileInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TFileInfo record);

    int updateByPrimaryKey(TFileInfo record);

	List<TFileInfo> selectFileByParams(TFileInfo fileInfo);
	
	List<TFileInfo> selectFileList(QueryInfo query);

}