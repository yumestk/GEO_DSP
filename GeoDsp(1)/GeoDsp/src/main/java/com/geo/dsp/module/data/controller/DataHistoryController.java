package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.entity.DataHistory;
import com.geo.dsp.module.data.service.DataHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data-history")
public class DataHistoryController {

    @Resource
    private DataHistoryService dataHistoryService;

    @GetMapping("/{uuid}")
    public Result<DataHistory> getByUuid(@PathVariable String uuid) {
        return dataHistoryService.getByUuid(uuid);
    }

    @GetMapping("/data/{dataUuid}")
    public Result<List<DataHistory>> listByData(@PathVariable String dataUuid) {
        return dataHistoryService.listByDataUuid(dataUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DataHistory dataHistory) {
        return dataHistoryService.create(dataHistory);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return dataHistoryService.deleteByUuid(uuid, updateBy);
    }
}
