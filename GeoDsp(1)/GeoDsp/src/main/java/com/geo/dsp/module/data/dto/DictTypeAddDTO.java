package com.geo.dsp.module.data.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 新增【字典类型】的表单 DTO
 * 对应前端第一个表单：添加字典类型
 */
@Data
public class DictTypeAddDTO {

    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    // 描述（可选）
    private String dictDesc;
}
