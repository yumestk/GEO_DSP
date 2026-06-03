package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.entity.OperationLog;

import java.util.List;

public interface OperationLogService {
    Result<PageVo<OperationLog>> pageList(String keyword, int pageNum, int pageSize);
    Result<OperationLog> getByUuid(String uuid);
    Result<List<OperationLog>> listByProjectUuid(String projectUuid);
    Result<Boolean> create(OperationLog operationLog);
    Result<Boolean> updateContent(String uuid, String content, Long updateBy);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
