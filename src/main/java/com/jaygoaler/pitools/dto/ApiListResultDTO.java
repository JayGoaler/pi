package com.jaygoaler.pitools.dto;

import com.jaygoaler.pitools.utils.DTO;
import lombok.Data;

import java.util.List;

/**
 * @Author: 杨暾
 * @Date: 2019/7/22 17:38
 * @Description: 接口返回结果传输类
 */
@Data
public class ApiListResultDTO implements DTO {
    /**
     * 状态码
     */
    private String code;
    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 信息
     */
    private String message;
    /**
     * 返回数据
     */
    private List<Object> data;
}
