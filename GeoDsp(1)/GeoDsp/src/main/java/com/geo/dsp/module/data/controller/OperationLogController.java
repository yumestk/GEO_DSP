package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.entity.OperationLog;
import com.geo.dsp.module.data.service.OperationLogService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operation-log")
public class OperationLogController {

    @Resource
    private OperationLogService operationLogService;

    @GetMapping("/list")
    public Result<PageVo<OperationLog>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return operationLogService.pageList(keyword, pageNum, pageSize);
    }

    @GetMapping("/{uuid}")
    public Result<OperationLog> getByUuid(@PathVariable String uuid) {
        return operationLogService.getByUuid(uuid);
    }

    @GetMapping("/project/{projectUuid}")
    public Result<List<OperationLog>> listByProject(@PathVariable String projectUuid) {
        return operationLogService.listByProjectUuid(projectUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody OperationLog operationLog) {
        return operationLogService.create(operationLog);
    }

    @PutMapping("/{uuid}/content")
    public Result<Boolean> updateContent(@PathVariable String uuid,
                                         @RequestParam String content,
                                         @RequestParam(defaultValue = "1") Long updateBy) {
        return operationLogService.updateContent(uuid, content, updateBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return operationLogService.deleteByUuid(uuid, updateBy);
    }
}
