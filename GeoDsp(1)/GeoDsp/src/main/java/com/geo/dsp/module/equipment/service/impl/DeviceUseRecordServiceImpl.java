package com.geo.dsp.module.equipment.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.equipment.dto.DeviceUseRecordDTO;
import com.geo.dsp.module.equipment.entity.UseRecord;
import com.geo.dsp.module.equipment.mapper.UseRecordMapper;
import com.geo.dsp.module.equipment.service.DeviceUseRecordService;
import com.geo.dsp.module.equipment.vo.DeviceUseRecordVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceUseRecordServiceImpl implements DeviceUseRecordService {

    @Resource
    private UseRecordMapper useRecordMapper;

    private DeviceUseRecordVO toVO(UseRecord e) {
        if (e == null) return null;
        DeviceUseRecordVO vo = new DeviceUseRecordVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<DeviceUseRecordVO> getByUuid(String uuid) {
        return Result.success(toVO(useRecordMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<DeviceUseRecordVO>> listByDeviceUuid(String deviceUuid) {
        List<DeviceUseRecordVO> list = useRecordMapper.listByDeviceUuid(deviceUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<Boolean> create(DeviceUseRecordDTO dto) {
        UseRecord entity = new UseRecord();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setDeviceId(dto.getDeviceId());
        entity.setProjectId(dto.getProjectId());
        entity.setEngineeringId(dto.getEngineeringId());
        entity.setTaskId(dto.getTaskId());
        entity.setUserId(dto.getUserId());
        entity.setUseStartTime(dto.getUseStartTime());
        entity.setUseEndTime(dto.getUseEndTime());
        entity.setNote(dto.getNote());
        entity.setIsDel(false);
        return useRecordMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> update(String uuid, DeviceUseRecordDTO dto) {
        UseRecord entity = useRecordMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("记录不存在");
        if (dto.getUseStartTime() != null) entity.setUseStartTime(dto.getUseStartTime());
        if (dto.getUseEndTime() != null) entity.setUseEndTime(dto.getUseEndTime());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        return useRecordMapper.update(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid) {
        return useRecordMapper.deleteByUuid(uuid) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
