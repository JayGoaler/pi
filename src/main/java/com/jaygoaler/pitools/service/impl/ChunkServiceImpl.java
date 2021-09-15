package com.jaygoaler.pitools.service.impl;

import com.jaygoaler.pitools.mapper.TChunkInfoMapper;
import com.jaygoaler.pitools.model.TChunkInfo;
import com.jaygoaler.pitools.service.ChunkService;
import com.jaygoaler.pitools.utils.SnowflakeIdWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class ChunkServiceImpl implements ChunkService {

	@Resource
	TChunkInfoMapper tChunkInfoMapper;
	
    @Override
    public int saveChunk(TChunkInfo chunk) {
    	chunk.setId(SnowflakeIdWorker.getUUID()+SnowflakeIdWorker.getUUID());
    	return tChunkInfoMapper.insertSelective(chunk);
    }

    @Override
    public ArrayList<Integer> checkChunk(TChunkInfo chunk) {
    	return tChunkInfoMapper.selectChunkNumbers(chunk);
    }

	@Override
	public boolean checkChunk(String identifier, Integer chunkNumber) {
		return false;
	}

}
