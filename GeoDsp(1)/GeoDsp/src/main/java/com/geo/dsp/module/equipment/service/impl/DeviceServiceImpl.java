package com.geo.dsp.module.equipment.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.equipment.dto.DeviceDTO;
import com.geo.dsp.module.equipment.entity.Device;
import com.geo.dsp.module.equipment.entity.DeviceType;
import com.geo.dsp.module.equipment.mapper.DeviceMapper;
import com.geo.dsp.module.equipment.mapper.DeviceTypeMapper;
import com.geo.dsp.module.equipment.service.DeviceService;
import com.geo.dsp.module.equipment.vo.DeviceVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private DeviceTypeMapper deviceTypeMapper;

    private DeviceVO toVO(Device e) {
        if (e == null) return null;
        DeviceVO vo = new DeviceVO();
        BeanUtils.copyProperties(e, vo);
        if (e.getDeviceTypeId() != null) {
            DeviceType dt = deviceTypeMapper.selectById(e.getDeviceTypeId());
            if (dt != null) vo.setDeviceTypeName(dt.getTypeName());
        }
        return vo;
    }

    @Override
    public Result<PageVo<DeviceVO>> pageList(String deviceNum, Long deviceTypeId, Integer status,
                                             int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<DeviceVO> list = deviceMapper.selectPage(deviceNum, deviceTypeId, status, offset, pageSize)
                .stream().map(this::toVO).collect(Collectors.toList());
        long total = deviceMapper.selectCount(deviceNum, deviceTypeId, status);
        PageVo<DeviceVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    public Result<DeviceVO> getByUuid(String uuid) {
        return Result.success(toVO(deviceMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<Boolean> create(DeviceDTO dto) {
        Device entity = new Device();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setCompanyId(dto.getCompanyId());
        entity.setDeviceNum(dto.getDeviceNum());
        entity.setDeviceTypeId(dto.getDeviceTypeId());
        entity.setAntennaNum(dto.getAntennaNum());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return deviceMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增设备失败");
    }

    @Override
    public Result<Boolean> update(String uuid, DeviceDTO dto) {
        Device entity = deviceMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("设备不存在");
        if (dto.getDeviceNum() != null) entity.setDeviceNum(dto.getDeviceNum());
        if (dto.getDeviceTypeId() != null) entity.setDeviceTypeId(dto.getDeviceTypeId());
        if (dto.getAntennaNum() != null) entity.setAntennaNum(dto.getAntennaNum());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return deviceMapper.update(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return deviceMapper.deleteByUuid(uuid) > 0 ? Result.success(true) : Result.fail("删除失败");
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy) {
        return deviceMapper.updateStatusByUuid(uuid, status) > 0 ? Result.success(true) : Result.fail("状态更新失败");
    }

    @Override
    public Result<Boolean> batchOperation(List<String> uuids, String action, Long updateBy) {
        if (uuids == null || uuids.isEmpty()) return Result.fail("参数为空");
        List<String> failed = new ArrayList<>();
        for (String uuid : uuids) {
            boolean ok;
            switch (action) {
                case "delete":
                    ok = deviceMapper.deleteByUuid(uuid) > 0;
                    break;
                case "enable":
                    ok = deviceMapper.updateStatusByUuid(uuid, 1) > 0;
                    break;
                case "disable":
                    ok = deviceMapper.updateStatusByUuid(uuid, 0) > 0;
                    break;
                default:
                    return Result.fail("不支持的操作: " + action);
            }
            if (!ok) failed.add(uuid);
        }
        return failed.isEmpty() ? Result.success(true) : Result.fail("部分失败: " + String.join(",", failed));
    }
}
