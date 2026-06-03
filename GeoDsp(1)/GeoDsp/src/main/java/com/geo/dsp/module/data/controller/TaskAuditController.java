package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.TaskAuditDTO;
import com.geo.dsp.module.data.service.TaskAuditService;
import com.geo.dsp.module.data.vo.TaskAuditVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-audit")
public class TaskAuditController {

    @Resource
    private TaskAuditService taskAuditService;

    @GetMapping("/{uuid}")
    public Result<TaskAuditVO> getByUuid(@PathVariable String uuid) {
        return taskAuditService.getByUuid(uuid);
    }

    @GetMapping("/task/{taskUuid}")
    public Result<List<TaskAuditVO>> listByTask(@PathVariable String taskUuid) {
        return taskAuditService.listByTaskUuid(taskUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody TaskAuditDTO dto) {
        return taskAuditService.create(dto);
    }

    @PutMapping("/{uuid}/result")
    public Result<Boolean> updateResult(@PathVariable String uuid,
                                        @RequestParam Integer auditResult,
                                        @RequestParam(required = false) String auditOpinion) {
        return taskAuditService.updateAuditResult(uuid, auditResult, auditOpinion);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return taskAuditService.deleteByUuid(uuid);
    }
}
