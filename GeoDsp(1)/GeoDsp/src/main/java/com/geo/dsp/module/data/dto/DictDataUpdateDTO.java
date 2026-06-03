package com.geo.dsp.module.data.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * 字典数据修改表单（前端第二个表单：编辑字典数据）
 */
@Data
public class DictDataUpdateDTO {

    @NotBlank(message = "字典UUID不能为空")
    private String uuid;

    @NotBlank(message = "字典键不能为空")
    private String dictKey;

    private String dictStrVal;

    private Double dictNumVal;

    private String dictDesc;
}
