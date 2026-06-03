package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DataSampleDTO;
import com.geo.dsp.module.data.service.DataSampleService;
import com.geo.dsp.module.data.vo.DataSampleVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data-sample")
public class DataSampleController {

    @Resource
    private DataSampleService dataSampleService;

    @GetMapping("/{uuid}")
    public Result<DataSampleVO> getByUuid(@PathVariable String uuid) {
        return dataSampleService.getByUuid(uuid);
    }

    @GetMapping("/point/{dataPointUuid}")
    public Result<List<DataSampleVO>> listByPoint(@PathVariable String dataPointUuid) {
        return dataSampleService.listByDataPointUuid(dataPointUuid);
    }

    @GetMapping("/task/{taskUuid}")
    public Result<List<DataSampleVO>> listByTask(@PathVariable String taskUuid) {
        return dataSampleService.listByTaskUuid(taskUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DataSampleDTO dto) {
        return dataSampleService.create(dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return dataSampleService.deleteByUuid(uuid, updateBy);
    }
}
