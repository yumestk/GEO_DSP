package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.dto.DataPointDTO;
import com.geo.dsp.module.data.entity.DataPoint;
import com.geo.dsp.module.data.mapper.DataPointMapper;
import com.geo.dsp.module.data.service.DataPointService;
import com.geo.dsp.module.data.vo.DataPointVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataPointServiceImpl implements DataPointService {

    @Resource
    private DataPointMapper dataPointMapper;

    private DataPointVO toVO(DataPoint e) {
        if (e == null) return null;
        DataPointVO vo = new DataPointVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<DataPointVO> getByUuid(String uuid) {
        return Result.success(toVO(dataPointMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<DataPointVO>> listByDataLineUuid(String dataLineUuid) {
        List<DataPointVO> list = dataPointMapper.listByDataLineUuid(dataLineUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<Boolean> create(DataPointDTO dto) {
        DataPoint entity = new DataPoint();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setLineId(dto.getLineId());
        entity.setPointNo(dto.getPointNo());
        entity.setXCoord(dto.getXCoord());
        entity.setYCoord(dto.getYCoord());
        entity.setZCoord(dto.getZCoord());
        entity.setCollectTime(dto.getCollectTime());
        entity.setCollectorId(dto.getCollectorId());
        entity.setDeviceId(dto.getDeviceId());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return dataPointMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> update(String uuid, DataPointDTO dto) {
        DataPoint entity = dataPointMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("作业点不存在");
        if (dto.getPointNo() != null) entity.setPointNo(dto.getPointNo());
        if (dto.getXCoord() != null) entity.setXCoord(dto.getXCoord());
        if (dto.getYCoord() != null) entity.setYCoord(dto.getYCoord());
        if (dto.getZCoord() != null) entity.setZCoord(dto.getZCoord());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return dataPointMapper.updateByUuid(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return dataPointMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }

    @Override
    public Result<Boolean> batchDeleteByDataLineUuid(String dataLineUuid, Long updateBy) {
        dataPointMapper.batchDeleteByDataLineUuid(dataLineUuid, updateBy);
        return Result.success(true);
    }
}
