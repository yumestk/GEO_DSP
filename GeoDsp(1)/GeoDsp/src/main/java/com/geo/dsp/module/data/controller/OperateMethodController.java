package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.OperateMethodDTO;
import com.geo.dsp.module.data.service.OperateMethodService;
import com.geo.dsp.module.data.vo.OperateMethodVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operate-method")
public class OperateMethodController {

    @Resource
    private OperateMethodService operateMethodService;

    @GetMapping("/list")
    public Result<List<OperateMethodVO>> listAll() {
        return operateMethodService.listAll();
    }

    @GetMapping("/{uuid}")
    public Result<OperateMethodVO> getByUuid(@PathVariable String uuid) {
        return operateMethodService.getByUuid(uuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody OperateMethodDTO dto) {
        return operateMethodService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody OperateMethodDTO dto) {
        return operateMethodService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return operateMethodService.deleteByUuid(uuid, updateBy);
    }
}
