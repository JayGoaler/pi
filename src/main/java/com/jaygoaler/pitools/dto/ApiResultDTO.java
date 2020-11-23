package com.jaygoaler.pitools.dto;

import com.jaygoaler.pitools.utils.DTO;
import com.jaygoaler.pitools.utils.ResponseTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: 杨暾
 * @Date: 2019/7/22 17:38
 * @Description: 接口返回结果传输类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResultDTO implements DTO {
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
    private Object data;

    public ApiResultDTO(String code, String message, boolean success, Object data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public static ApiResultDTO getSuccessInfo() {
        return getSuccessInfo(ResponseTypes.SUCCESS.getDesc());
    }

    public static ApiResultDTO getSuccessInfo(String msg) {
        return getSuccessInfo(msg, null);
    }

    public static ApiResultDTO getSuccessInfo(Object data) {
        return getSuccessInfo(ResponseTypes.SUCCESS.getDesc(), data);
    }

    public static ApiResultDTO getSuccessInfo(String msg, Object data) {
        return new ApiResultDTO(ResponseTypes.SUCCESS.getCode(), msg, true, data);
    }

    public static ApiResultDTO getFailedInfo(String msg) {
        return getFailedInfo(msg, null);
    }

    public static ApiResultDTO getFailedInfo(String msg, Object data) {
        return new ApiResultDTO(ResponseTypes.FAIL.getCode(), msg, false, data);
    }
}
