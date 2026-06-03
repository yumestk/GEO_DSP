package com.geo.dsp.module.equipment.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.equipment.dto.DataWorksetDTO;
import com.geo.dsp.module.equipment.service.DataWorksetService;
import com.geo.dsp.module.equipment.vo.DataWorksetVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/data-workset")
public class DataWorksetController {

    @Resource
    private DataWorksetService dataWorksetService;

    @GetMapping("/list")
    public Result<PageVo<DataWorksetVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return dataWorksetService.pageList(keyword, pageNum, pageSize);
    }

    @GetMapping("/{uuid}")
    public Result<DataWorksetVO> getByUuid(@PathVariable String uuid) {
        return dataWorksetService.getByUuid(uuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DataWorksetDTO dto) {
        return dataWorksetService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody DataWorksetDTO dto) {
        return dataWorksetService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return dataWorksetService.deleteByUuid(uuid);
    }
}
