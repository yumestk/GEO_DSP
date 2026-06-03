package com.geo.dsp.module.equipment.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.equipment.dto.DeviceTypeDTO;
import com.geo.dsp.module.equipment.service.DeviceTypeService;
import com.geo.dsp.module.equipment.vo.DeviceTypeVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device/type")
public class DeviceTypeController {

    @Resource
    private DeviceTypeService deviceTypeService;

    @GetMapping("/list")
    public Result<List<DeviceTypeVO>> listAll() {
        return deviceTypeService.listAll();
    }

    @GetMapping("/{uuid}")
    public Result<DeviceTypeVO> getByUuid(@PathVariable String uuid) {
        return deviceTypeService.getByUuid(uuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DeviceTypeDTO dto) {
        return deviceTypeService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody DeviceTypeDTO dto) {
        return deviceTypeService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return deviceTypeService.deleteByUuid(uuid);
    }
}
