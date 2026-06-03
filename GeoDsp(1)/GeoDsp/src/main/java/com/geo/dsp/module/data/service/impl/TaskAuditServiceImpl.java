package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.dto.TaskAuditDTO;
import com.geo.dsp.module.data.entity.TaskAudit;
import com.geo.dsp.module.data.mapper.TaskAuditMapper;
import com.geo.dsp.module.data.service.TaskAuditService;
import com.geo.dsp.module.data.vo.TaskAuditVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskAuditServiceImpl implements TaskAuditService {

    @Resource
    private TaskAuditMapper taskAuditMapper;

    private TaskAuditVO toVO(TaskAudit e) {
        if (e == null) return null;
        TaskAuditVO vo = new TaskAuditVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<TaskAuditVO> getByUuid(String uuid) {
        return Result.success(toVO(taskAuditMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<TaskAuditVO>> listByTaskUuid(String taskUuid) {
        return Result.success(taskAuditMapper.listByTaskUuid(taskUuid).stream().map(this::toVO).collect(Collectors.toList()));
    }

    @Override
    public Result<Boolean> create(TaskAuditDTO dto) {
        TaskAudit entity = new TaskAudit();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setTaskId(dto.getTaskId());
        entity.setAuditorId(dto.getAuditorId());
        entity.setAuditTime(dto.getAuditTime() != null ? dto.getAuditTime() : OffsetDateTime.now());
        entity.setAuditResult(dto.getAuditResult());
        entity.setAuditOpinion(dto.getAuditOpinion());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return taskAuditMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> updateAuditResult(String uuid, Integer auditResult, String auditOpinion) {
        return taskAuditMapper.updateAuditResultByUuid(uuid, auditResult, auditOpinion) > 0
                ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid) {
        return taskAuditMapper.deleteByUuid(uuid) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
