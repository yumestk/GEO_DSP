package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.DataLineDTO;
import com.geo.dsp.module.data.service.DataLineService;
import com.geo.dsp.module.data.vo.DataLineVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/data-line")
public class DataLineController {

    @Resource
    private DataLineService dataLineService;

    @GetMapping("/{uuid}")
    public Result<DataLineVO> getByUuid(@PathVariable String uuid) {
        return dataLineService.getByUuid(uuid);
    }

    @GetMapping("/engineering/{engineeringUuid}")
    public Result<List<DataLineVO>> listByEngineering(@PathVariable String engineeringUuid) {
        return dataLineService.listByEngineeringUuid(engineeringUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DataLineDTO dto) {
        return dataLineService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody DataLineDTO dto) {
        return dataLineService.update(uuid, dto);
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> updateStatus(@PathVariable String uuid,
                                        @RequestParam Integer status,
                                        @RequestParam(defaultValue = "1") Long updateBy) {
        return dataLineService.updateStatus(uuid, status, updateBy);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return dataLineService.deleteByUuid(uuid, updateBy);
    }
}
