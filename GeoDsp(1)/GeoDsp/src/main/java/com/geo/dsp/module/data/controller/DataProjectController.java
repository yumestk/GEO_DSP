package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.entity.DataProject;
import com.geo.dsp.module.data.service.DataProjectService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data-project")
public class DataProjectController {

    @Resource
    private DataProjectService dataProjectService;

    @GetMapping("/{uuid}")
    public Result<DataProject> getByUuid(@PathVariable String uuid) {
        return dataProjectService.getByUuid(uuid);
    }

    @GetMapping("/project/{projectUuid}")
    public Result<List<DataProject>> listByProject(@PathVariable String projectUuid) {
        return dataProjectService.listByProjectUuid(projectUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DataProject dataProject) {
        return dataProjectService.create(dataProject);
    }

    @PutMapping
    public Result<Boolean> update(@RequestBody DataProject dataProject) {
        return dataProjectService.update(dataProject);
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> updateStatus(@PathVariable String uuid,
                                        @RequestParam Integer status,
                                        @RequestParam(defaultValue = "1") Long updateBy) {
        return dataProjectService.updateStatus(uuid, status, updateBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return dataProjectService.deleteByUuid(uuid, updateBy);
    }
}
