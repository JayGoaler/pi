package com.jaygoaler.pitools.controller;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @author zhanglj
 * @version 1.0.0
 * @date 2020/7/27 9:53
 * <p>
 * description: 控制层基础类
 */
public class BaseApiController {

    /**
     * 验证参数是否满足要求
     *
     * @param result
     * @return
     */
    protected String checkValidated(BindingResult result) {
        String errMsg = null;
        if (result.hasErrors()) {
            List<ObjectError> objectErrors = result.getAllErrors();
            errMsg = String.valueOf(objectErrors.get(0).getDefaultMessage());
        }
        return errMsg;
    }

}
