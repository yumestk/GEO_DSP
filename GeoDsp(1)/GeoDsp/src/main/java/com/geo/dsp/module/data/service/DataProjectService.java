package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.entity.DataProject;

import java.util.List;

public interface DataProjectService {
    Result<DataProject> getByUuid(String uuid);
    Result<List<DataProject>> listByProjectUuid(String projectUuid);
    Result<Boolean> create(DataProject dataProject);
    Result<Boolean> update(DataProject dataProject);
    Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
