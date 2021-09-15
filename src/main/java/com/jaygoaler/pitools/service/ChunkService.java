package com.jaygoaler.pitools.service;

import com.jaygoaler.pitools.model.TChunkInfo;

import java.util.ArrayList;

/**
 * 文件块处理
 * @author 洋葱骑士
 *
 */
public interface ChunkService {
    /**
     * 保存文件块
     *
     * @param chunk
     */
    int saveChunk(TChunkInfo chunk);

    /**
     * 检查文件块是否存在
     *
     * @param identifier
     * @param chunkNumber
     * @return
     */
    boolean checkChunk(String identifier, Integer chunkNumber);
    
    /**
     * 查询哪些文件块已经上传
     * @param chunk
     * @return
     */
    ArrayList<Integer> checkChunk(TChunkInfo chunk);
}
