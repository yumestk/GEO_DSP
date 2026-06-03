package com.geo.dsp.module.equipment.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.equipment.dto.DeviceDTO;
import com.geo.dsp.module.equipment.vo.DeviceVO;

public interface DeviceService {
    Result<PageVo<DeviceVO>> pageList(String deviceNum, Long deviceTypeId, Integer status, int pageNum, int pageSize);
    Result<DeviceVO> getByUuid(String uuid);
    Result<Boolean> create(DeviceDTO dto);
    Result<Boolean> update(String uuid, DeviceDTO dto);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
    Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy);
    Result<Boolean> batchOperation(java.util.List<String> uuids, String action, Long updateBy);
}
