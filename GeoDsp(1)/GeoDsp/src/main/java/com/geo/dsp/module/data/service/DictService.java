package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DictDataAddDTO;
import com.geo.dsp.module.data.dto.DictDataUpdateDTO;
import com.geo.dsp.module.data.dto.DictTypeAddDTO;
import com.geo.dsp.module.data.dto.DictTypeUpdateDTO;
import com.geo.dsp.module.data.entity.Dict;
import java.util.List;

public interface DictService {

    // ===================== 字典类型（表单1） =====================
    // 查询所有字典类型
    Result<List<Dict>> getDictTypeList();

    // 新增字典类型
    Result<Boolean> addDictType(DictTypeAddDTO dto, Long createBy);

    //修改字典类型
    Result<Boolean> updateDictType(DictTypeUpdateDTO dto, Long updateBy);
    // ===================== 字典数据（表单2） =====================
    // 根据编码查询字典数据
    Result<List<Dict>> getDictDataList(String dictCode);

    // 新增字典数据
    Result<Boolean> addDictData(DictDataAddDTO dto, Long createBy);

    //修改字典数据
    Result<Boolean> updateDictData(DictDataUpdateDTO dto, Long updateBy);

    // 删除
    Result<Boolean> deleteDict(String uuid);
}