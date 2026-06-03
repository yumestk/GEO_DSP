package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DataLineDTO;
import com.geo.dsp.module.data.vo.DataLineVO;

import java.util.List;

public interface DataLineService {
    Result<DataLineVO> getByUuid(String uuid);
    Result<List<DataLineVO>> listByEngineeringUuid(String engineeringUuid);
    Result<Boolean> create(DataLineDTO dto);
    Result<Boolean> update(String uuid, DataLineDTO dto);
    Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
