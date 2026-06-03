package com.geo.dsp.module.data.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 新增【字典数据】的表单 DTO
 * 对应前端第二个表单：添加字典数据
 */
@Data
public class DictDataAddDTO {

    @NotBlank(message = "所属字典类型编码不能为空")
    private String dictCode;

    @NotBlank(message = "字典键不能为空")
    private String dictKey;

    // 字典显示值
    private String dictStrVal;

    // 字典数字值
    private Double dictNumVal;

    // 描述
    private String dictDesc;
}
