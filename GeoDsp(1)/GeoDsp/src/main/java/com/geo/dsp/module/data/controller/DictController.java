package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DictDataAddDTO;
import com.geo.dsp.module.data.dto.DictDataUpdateDTO;
import com.geo.dsp.module.data.dto.DictTypeAddDTO;
import com.geo.dsp.module.data.dto.DictTypeUpdateDTO;
import com.geo.dsp.module.data.entity.Dict;
import com.geo.dsp.module.data.service.DictService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 字典管理（单表，支持两个前端表单）
 * 1. 字典类型表单
 * 2. 字典数据表单
 */
@RestController
@RequestMapping("/api/system/dict")
public class DictController {

    @Resource
    private DictService dictService;

    // ===================== 【表单1】字典类型接口 =====================
    /**
     * 查询字典类型列表
     */
    @GetMapping("/type/list")
    public Result<List<Dict>> typeList() {
        return dictService.getDictTypeList();
    }

    /**
     * 新增字典类型（前端第一个表单提交）
     */
    @PostMapping("/type/add")
    public Result<Boolean> addType(
            @Valid @RequestBody DictTypeAddDTO dto,
            @RequestParam(value = "createBy", required = false, defaultValue = "1") Long createBy
    ) {
        return dictService.addDictType(dto, createBy);
    }

    /**
     * 修改字典类型（前端第一个表单：编辑）
     */
    @PutMapping("/type/update")
    public Result<Boolean> updateType(
            @Valid @RequestBody DictTypeUpdateDTO dto,
            @RequestParam(value = "updateBy", required = false, defaultValue = "1") Long updateBy
    ) {
        return dictService.updateDictType(dto, updateBy);
    }
    // ===================== 【表单2】字典数据接口 =====================
    /**
     * 根据字典编码查询数据列表
     */
    @GetMapping("/data/list")
    public Result<List<Dict>> dataList(@RequestParam String dictCode) {
        return dictService.getDictDataList(dictCode);
    }

    /**
     * 新增字典数据（前端第二个表单提交）
     */
    @PostMapping("/data/add")
    public Result<Boolean> addData(
            @Valid @RequestBody DictDataAddDTO dto,
            @RequestParam(value = "createBy", required = false, defaultValue = "1") Long createBy
    ) {
        return dictService.addDictData(dto, createBy);
    }

    /**
     * 修改字典数据（前端第二个表单：编辑）
     */
    @PutMapping("/data/update")
    public Result<Boolean> updateData(
            @Valid @RequestBody DictDataUpdateDTO dto,
            @RequestParam(value = "updateBy", required = false, defaultValue = "1") Long updateBy
    ) {
        return dictService.updateDictData(dto, updateBy);
    }
    // ===================== 删除 =====================
    @DeleteMapping("/delete/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return dictService.deleteDict(uuid);
    }
}
