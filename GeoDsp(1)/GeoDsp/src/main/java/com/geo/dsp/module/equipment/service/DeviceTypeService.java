package com.geo.dsp.module.equipment.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.equipment.dto.DeviceTypeDTO;
import com.geo.dsp.module.equipment.vo.DeviceTypeVO;

import java.util.List;

public interface DeviceTypeService {
    Result<List<DeviceTypeVO>> listAll();
    Result<DeviceTypeVO> getByUuid(String uuid);
    Result<Boolean> create(DeviceTypeDTO dto);
    Result<Boolean> update(String uuid, DeviceTypeDTO dto);
    Result<Boolean> deleteByUuid(String uuid);
}
