package com.jaygoaler.pitools.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @program: pi
 * @description: 叶子节点信息获取参数
 * @author: JayGoal
 * @create: 2020-12-04 16:36
 **/
@Data
@ApiModel(description = "叶子节点信息获取参数")
public class GetLeafRequestDto {

    /**
     * 获取类型
     */
    @NotNull
    @ApiModelProperty(name = "获取类型", value = "1",required = true)
    private Integer type;

    /**
     * 当前id
     */
    @ApiModelProperty(name = "当前id", value = "null")
    private String id;
}
