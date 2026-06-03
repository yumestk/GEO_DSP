package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.dto.DataLineDTO;
import com.geo.dsp.module.data.entity.DataLine;
import com.geo.dsp.module.data.mapper.DataLineMapper;
import com.geo.dsp.module.data.service.DataLineService;
import com.geo.dsp.module.data.vo.DataLineVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataLineServiceImpl implements DataLineService {

    @Resource
    private DataLineMapper dataLineMapper;

    private DataLineVO toVO(DataLine e) {
        if (e == null) return null;
        DataLineVO vo = new DataLineVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<DataLineVO> getByUuid(String uuid) {
        return Result.success(toVO(dataLineMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<DataLineVO>> listByEngineeringUuid(String engineeringUuid) {
        List<DataLineVO> list = dataLineMapper.listByEngineeringUuid(engineeringUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<Boolean> create(DataLineDTO dto) {
        DataLine entity = new DataLine();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setUse(dto.getUse());
        entity.setName(dto.getName());
        entity.setEngineeringId(dto.getEngineeringId());
        entity.setType(dto.getType());
        entity.setListDataPoint(dto.getListDataPoint());
        entity.setPlanStartTime(dto.getPlanStartTime());
        entity.setPlanEndTime(dto.getPlanEndTime());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return dataLineMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> update(String uuid, DataLineDTO dto) {
        DataLine entity = dataLineMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("作业线不存在");
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return dataLineMapper.updateByUuid(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy) {
        return dataLineMapper.updateStatusByUuid(uuid, status, updateBy) > 0
                ? Result.success(true) : Result.fail("状态更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return dataLineMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
