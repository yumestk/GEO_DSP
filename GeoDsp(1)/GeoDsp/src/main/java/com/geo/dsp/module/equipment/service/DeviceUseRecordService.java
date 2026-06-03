package com.geo.dsp.module.equipment.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.equipment.dto.DeviceUseRecordDTO;
import com.geo.dsp.module.equipment.vo.DeviceUseRecordVO;

import java.util.List;

public interface DeviceUseRecordService {
    Result<DeviceUseRecordVO> getByUuid(String uuid);
    Result<List<DeviceUseRecordVO>> listByDeviceUuid(String deviceUuid);
    Result<Boolean> create(DeviceUseRecordDTO dto);
    Result<Boolean> update(String uuid, DeviceUseRecordDTO dto);
    Result<Boolean> deleteByUuid(String uuid);
}
