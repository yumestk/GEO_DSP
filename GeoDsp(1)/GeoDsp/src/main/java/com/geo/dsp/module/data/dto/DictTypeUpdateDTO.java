package com.geo.dsp.module.data.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 字典类型修改表单（前端第一个表单：编辑字典类型）
 */
@Data
public class DictTypeUpdateDTO {

    @NotBlank(message = "字典UUID不能为空")
    private String uuid;

    @NotBlank(message = "字典名称不能为空")
    private String dictName;

    @NotBlank(message = "字典编码不能为空")
    private String dictCode;

    private String dictDesc;
}
