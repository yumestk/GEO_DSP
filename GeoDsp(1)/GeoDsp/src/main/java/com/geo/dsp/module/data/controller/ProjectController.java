package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.dto.ProjectDTO;
import com.geo.dsp.module.data.service.ProjectService;
import com.geo.dsp.module.data.vo.ProjectVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    @Resource
    private ProjectService projectService;

    @GetMapping("/list")
    public Result<PageVo<ProjectVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") java.time.LocalDate startDate,
            @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") java.time.LocalDate endDate,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        java.time.OffsetDateTime startTime = startDate != null ? startDate.atStartOfDay(java.time.ZoneOffset.ofHours(8)).toOffsetDateTime() : null;
        java.time.OffsetDateTime endTime = endDate != null ? endDate.atTime(23, 59, 59).atOffset(java.time.ZoneOffset.ofHours(8)) : null;
        return projectService.pageList(keyword, companyId, startTime, endTime, status, pageNum, pageSize);
    }

    @GetMapping("/{uuid}")
    public Result<ProjectVO> getByUuid(@PathVariable String uuid) {
        return projectService.getByUuid(uuid);
    }

    @GetMapping("/company/{companyUuid}")
    public Result<List<ProjectVO>> listByCompany(@PathVariable String companyUuid) {
        return projectService.listByCompanyUuid(companyUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody ProjectDTO dto) {
        return projectService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody ProjectDTO dto) {
        return projectService.update(uuid, dto);
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> updateStatus(@PathVariable String uuid,
                                        @RequestParam Integer status,
                                        @RequestParam(defaultValue = "1") Long updateBy) {
        return projectService.updateStatus(uuid, status, updateBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return projectService.deleteByUuid(uuid, updateBy);
    }
}
