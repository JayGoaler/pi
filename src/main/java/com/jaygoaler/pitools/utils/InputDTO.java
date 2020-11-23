package com.jaygoaler.pitools.utils;

import org.modelmapper.ModelMapper;

import java.lang.reflect.Type;

/**
 * @author zhanglj
 * @data 2020/9/17 2:39 下午
 */
public interface InputDTO<T> extends DTO {
    default T map(Type ofType) {
        ModelMapper mapper = new ModelMapper();
        return mapper.map(this, ofType);
    }
}
