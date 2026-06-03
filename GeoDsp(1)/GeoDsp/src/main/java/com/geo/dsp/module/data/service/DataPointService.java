package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DataPointDTO;
import com.geo.dsp.module.data.vo.DataPointVO;

import java.util.List;

public interface DataPointService {
    Result<DataPointVO> getByUuid(String uuid);
    Result<List<DataPointVO>> listByDataLineUuid(String dataLineUuid);
    Result<Boolean> create(DataPointDTO dto);
    Result<Boolean> update(String uuid, DataPointDTO dto);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
    Result<Boolean> batchDeleteByDataLineUuid(String dataLineUuid, Long updateBy);
}
