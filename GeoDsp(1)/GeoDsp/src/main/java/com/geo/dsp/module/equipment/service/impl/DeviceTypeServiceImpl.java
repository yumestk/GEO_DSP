package com.geo.dsp.module.equipment.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.equipment.dto.DeviceTypeDTO;
import com.geo.dsp.module.equipment.entity.DeviceType;
import com.geo.dsp.module.equipment.mapper.DeviceTypeMapper;
import com.geo.dsp.module.equipment.service.DeviceTypeService;
import com.geo.dsp.module.equipment.vo.DeviceTypeVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceTypeServiceImpl implements DeviceTypeService {

    @Resource
    private DeviceTypeMapper deviceTypeMapper;

    private DeviceTypeVO toVO(DeviceType e) {
        if (e == null) return null;
        DeviceTypeVO vo = new DeviceTypeVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<List<DeviceTypeVO>> listAll() {
        List<DeviceTypeVO> list = deviceTypeMapper.listAll().stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<DeviceTypeVO> getByUuid(String uuid) {
        return Result.success(toVO(deviceTypeMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<Boolean> create(DeviceTypeDTO dto) {
        DeviceType entity = new DeviceType();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setTypeName(dto.getTypeName());
        entity.setTypeDesc(dto.getTypeDesc());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return deviceTypeMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> update(String uuid, DeviceTypeDTO dto) {
        DeviceType entity = deviceTypeMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("设备类型不存在");
        if (dto.getTypeName() != null) entity.setTypeName(dto.getTypeName());
        if (dto.getTypeDesc() != null) entity.setTypeDesc(dto.getTypeDesc());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return deviceTypeMapper.update(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid) {
        return deviceTypeMapper.deleteByUuid(uuid) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
