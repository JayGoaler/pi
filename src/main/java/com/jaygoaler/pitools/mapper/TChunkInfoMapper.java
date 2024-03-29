package com.jaygoaler.pitools.mapper;


import com.jaygoaler.pitools.model.TChunkInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface TChunkInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(TChunkInfo record);

    int insertSelective(TChunkInfo record);

    TChunkInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TChunkInfo record);

    int updateByPrimaryKey(TChunkInfo record);
    
    ArrayList<Integer> selectChunkNumbers(TChunkInfo record);
}