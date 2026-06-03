package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DataPointDTO;
import com.geo.dsp.module.data.service.DataPointService;
import com.geo.dsp.module.data.vo.DataPointVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data-point")
public class DataPointController {

    @Resource
    private DataPointService dataPointService;

    @GetMapping("/{uuid}")
    public Result<DataPointVO> getByUuid(@PathVariable String uuid) {
        return dataPointService.getByUuid(uuid);
    }

    @GetMapping("/line/{dataLineUuid}")
    public Result<List<DataPointVO>> listByLine(@PathVariable String dataLineUuid) {
        return dataPointService.listByDataLineUuid(dataLineUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DataPointDTO dto) {
        return dataPointService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody DataPointDTO dto) {
        return dataPointService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return dataPointService.deleteByUuid(uuid, updateBy);
    }

    @DeleteMapping("/batch/line/{dataLineUuid}")
    public Result<Boolean> batchDeleteByLine(@PathVariable String dataLineUuid,
                                             @RequestParam(defaultValue = "1") Long updateBy) {
        return dataPointService.batchDeleteByDataLineUuid(dataLineUuid, updateBy);
    }
}
