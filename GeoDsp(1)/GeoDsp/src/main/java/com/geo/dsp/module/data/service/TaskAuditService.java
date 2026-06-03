package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.TaskAuditDTO;
import com.geo.dsp.module.data.vo.TaskAuditVO;

import java.util.List;

public interface TaskAuditService {
    Result<TaskAuditVO> getByUuid(String uuid);
    Result<List<TaskAuditVO>> listByTaskUuid(String taskUuid);
    Result<Boolean> create(TaskAuditDTO dto);
    Result<Boolean> updateAuditResult(String uuid, Integer auditResult, String auditOpinion);
    Result<Boolean> deleteByUuid(String uuid);
}
