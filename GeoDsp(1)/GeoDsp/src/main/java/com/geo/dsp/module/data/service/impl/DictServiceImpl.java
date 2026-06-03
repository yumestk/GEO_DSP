package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DictDataAddDTO;
import com.geo.dsp.module.data.dto.DictDataUpdateDTO;
import com.geo.dsp.module.data.dto.DictTypeAddDTO;
import com.geo.dsp.module.data.dto.DictTypeUpdateDTO;
import com.geo.dsp.module.data.entity.Dict;
import com.geo.dsp.module.data.mapper.DictMapper;
import com.geo.dsp.module.data.service.DictService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.Resource;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Resource
    private DictMapper dictMapper;

    // ===================== 字典类型（表单1） =====================
    @Override
    public Result<List<Dict>> getDictTypeList() {
        return Result.success(dictMapper.listDictType());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> addDictType(DictTypeAddDTO dto, Long createBy) {
        Dict dict = new Dict();
        // 字典类型专用字段
        dict.setDictName(dto.getDictName());
        dict.setDictCode(dto.getDictCode());
        dict.setDictDesc(dto.getDictDesc());
        // 公共字段
        dict.setCreateBy(createBy);
        dict.setUpdateBy(createBy);
        dict.setIsDel(false);

        return dictMapper.insert(dict) > 0
                ? Result.success(true)
                : Result.fail("新增字典类型失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateDictType(DictTypeUpdateDTO dto, Long updateBy) {
        Dict dict = new Dict();
        dict.setUuid(dto.getUuid());
        dict.setDictName(dto.getDictName());
        dict.setDictCode(dto.getDictCode());
        dict.setDictDesc(dto.getDictDesc());
        dict.setUpdateBy(updateBy);

        return dictMapper.updateByUuid(dict) > 0
                ? Result.success(true)
                : Result.fail("修改字典类型失败");
    }
    // ===================== 字典数据（表单2） =====================
    @Override
    public Result<List<Dict>> getDictDataList(String dictCode) {
        return Result.success(dictMapper.listDictDataByCode(dictCode));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> addDictData(DictDataAddDTO dto, Long createBy) {
        Dict dict = new Dict();
        // 字典数据专用字段
        dict.setDictCode(dto.getDictCode());
        dict.setDictKey(dto.getDictKey());
        dict.setDictStrVal(dto.getDictStrVal());
        dict.setDictNumVal(dto.getDictNumVal());
        dict.setDictDesc(dto.getDictDesc());
        // 公共字段
        dict.setCreateBy(createBy);
        dict.setUpdateBy(createBy);
        dict.setIsDel(false);

        return dictMapper.insert(dict) > 0
                ? Result.success(true)
                : Result.fail("新增字典数据失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> updateDictData(DictDataUpdateDTO dto, Long updateBy) {
        Dict dict = new Dict();
        dict.setUuid(dto.getUuid());
        dict.setDictKey(dto.getDictKey());
        dict.setDictStrVal(dto.getDictStrVal());
        dict.setDictNumVal(dto.getDictNumVal());
        dict.setDictDesc(dto.getDictDesc());
        dict.setUpdateBy(updateBy);

        return dictMapper.updateByUuid(dict) > 0
                ? Result.success(true)
                : Result.fail("修改字典数据失败");
    }
    // ===================== 删除 =====================
    @Override
    public Result<Boolean> deleteDict(String uuid) {
        return dictMapper.deleteByUuid(uuid) > 0
                ? Result.success(true)
                : Result.fail("删除失败");
    }
}
