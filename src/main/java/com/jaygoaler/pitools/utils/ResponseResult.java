package com.jaygoaler.pitools.utils;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author JayGoal
 */
@ApiModel("统一响应模型")
public class ResponseResult implements DTO {
    @ApiModelProperty(value = "状态码")
    private String code;
    @ApiModelProperty(value = "是否成功")
    private boolean success;
    @ApiModelProperty(value = "响应消息")
    private String message;
    @ApiModelProperty(value = "数据对象")
    private Object data;

    public ResponseResult() {
    }

    public ResponseResult(Object data) {
        if (data instanceof ResponseTypes) {
            boolean success = data == ResponseTypes.SUCCESS;
            this.success = success;
            this.code = success ? ResponseTypes.SUCCESS.getCode() : ((ResponseTypes)data).getCode();
            this.message = success ? ResponseTypes.SUCCESS.getDesc() : ((ResponseTypes)data).getDesc();
        } else {
            this.success = true;
            this.code = ResponseTypes.SUCCESS.getCode();
            this.message = ResponseTypes.SUCCESS.getDesc();
            this.data = data;
        }

    }

    public ResponseResult(String message, Object data) {
        this.success = true;
        this.code = ResponseTypes.SUCCESS.getCode();
        this.message = message;
        this.data = data;
    }

    public ResponseResult(ResponseTypes type, String message) {
        this.success = type.equals(ResponseTypes.SUCCESS);
        this.code = type.getCode();
        this.message = message;
        this.data = null;
    }

    public ResponseResult(ResponseTypes type, String message, Object data) {
        this.success = type.equals(ResponseTypes.SUCCESS);
        this.code = type.getCode();
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Boolean success, String code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(String code, String message, boolean success, Object data) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static ResponseResult builder() {
        return new ResponseResult();
    }

    public ResponseResult success(Object data) {
        this.success = true;
        this.code = ResponseTypes.SUCCESS.getCode();
        this.data = data;
        return this;
    }

    public ResponseResult message(String message) {
        this.message = message;
        return this;
    }

    public static ResponseResult getSuccessInfo() {
        return getSuccessInfo(ResponseTypes.SUCCESS.getDesc());
    }

    public static ResponseResult getSuccessInfo(String msg) {
        return getSuccessInfo(msg, null);
    }

    public static ResponseResult getSuccessInfo(Object data) {
        return getSuccessInfo(ResponseTypes.SUCCESS.getDesc(), data);
    }

    public static ResponseResult getSuccessInfo(String msg, Object data) {
        return new ResponseResult(ResponseTypes.SUCCESS.getCode(), msg, true, data);
    }

    public static ResponseResult getFailedInfo(String msg) {
        return getFailedInfo(msg, null);
    }

    public static ResponseResult getFailedInfo(String msg, Object data) {
        return new ResponseResult(ResponseTypes.FAIL.getCode(), msg, false, data);
    }

    @Override
    public String toString() {
        return "ResponseResult{" +
                "code='" + code + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

