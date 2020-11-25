package com.jaygoaler.pitools.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: pi
 * @description: 文件信息
 * @author: JayGoal
 * @create: 2020-11-25 10:08
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileSimpleInfo implements Serializable {
    /**
     * 文件Id
     */
    private String id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 父节点ID
     */
    private String parentId;
}
