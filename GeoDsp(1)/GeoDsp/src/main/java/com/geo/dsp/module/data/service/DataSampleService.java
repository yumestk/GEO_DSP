package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DataSampleDTO;
import com.geo.dsp.module.data.vo.DataSampleVO;

import java.util.List;

public interface DataSampleService {
    Result<DataSampleVO> getByUuid(String uuid);
    Result<List<DataSampleVO>> listByDataPointUuid(String dataPointUuid);
    Result<List<DataSampleVO>> listByTaskUuid(String taskUuid);
    Result<Boolean> create(DataSampleDTO dto);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
