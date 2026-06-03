package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.entity.DataHistory;

import java.util.List;

public interface DataHistoryService {
    Result<DataHistory> getByUuid(String uuid);
    Result<List<DataHistory>> listByDataUuid(String dataUuid);
    Result<Boolean> create(DataHistory dataHistory);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
