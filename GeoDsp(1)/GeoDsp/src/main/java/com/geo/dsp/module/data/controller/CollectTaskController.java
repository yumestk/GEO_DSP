package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.dto.CollectTaskDTO;
import com.geo.dsp.module.data.service.CollectTaskService;
import com.geo.dsp.module.data.vo.CollectTaskVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collect-task")
public class CollectTaskController {

    @Resource
    private CollectTaskService collectTaskService;

    @GetMapping("/list")
    public Result<PageVo<CollectTaskVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectUuid,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return collectTaskService.pageList(keyword, projectUuid, status, pageNum, pageSize);
    }

    @GetMapping("/{uuid}")
    public Result<CollectTaskVO> getByUuid(@PathVariable String uuid) {
        return collectTaskService.getByUuid(uuid);
    }

    @GetMapping("/project/{projectUuid}")
    public Result<List<CollectTaskVO>> listByProject(@PathVariable String projectUuid) {
        return collectTaskService.listByProjectUuid(projectUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody CollectTaskDTO dto) {
        return collectTaskService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody CollectTaskDTO dto) {
        return collectTaskService.update(uuid, dto);
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> updateStatus(@PathVariable String uuid,
                                        @RequestParam Integer status,
                                        @RequestParam(defaultValue = "1") Long updateBy) {
        return collectTaskService.updateStatus(uuid, status, updateBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return collectTaskService.deleteByUuid(uuid, updateBy);
    }
}
