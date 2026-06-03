package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.dto.DataSampleDTO;
import com.geo.dsp.module.data.entity.DataSample;
import com.geo.dsp.module.data.mapper.DataSampleMapper;
import com.geo.dsp.module.data.service.DataSampleService;
import com.geo.dsp.module.data.vo.DataSampleVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataSampleServiceImpl implements DataSampleService {

    @Resource
    private DataSampleMapper dataSampleMapper;

    private DataSampleVO toVO(DataSample e) {
        if (e == null) return null;
        DataSampleVO vo = new DataSampleVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<DataSampleVO> getByUuid(String uuid) {
        return Result.success(toVO(dataSampleMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<DataSampleVO>> listByDataPointUuid(String dataPointUuid) {
        List<DataSampleVO> list = dataSampleMapper.listByDataPointUuid(dataPointUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<List<DataSampleVO>> listByTaskUuid(String taskUuid) {
        List<DataSampleVO> list = dataSampleMapper.listByTaskUuid(taskUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<Boolean> create(DataSampleDTO dto) {
        DataSample entity = new DataSample();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setPointId(dto.getPointId());
        entity.setSampleValue(dto.getSampleValue());
        entity.setSampleUnit(dto.getSampleUnit());
        entity.setSampleTime(dto.getSampleTime());
        entity.setDeviceId(dto.getDeviceId());
        entity.setCalibrationValue(dto.getCalibrationValue());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return dataSampleMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return dataSampleMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
