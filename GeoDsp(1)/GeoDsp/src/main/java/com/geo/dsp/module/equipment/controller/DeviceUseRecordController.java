package com.geo.dsp.module.equipment.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.equipment.dto.DeviceUseRecordDTO;
import com.geo.dsp.module.equipment.service.DeviceUseRecordService;
import com.geo.dsp.module.equipment.vo.DeviceUseRecordVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/device/use-record")
public class DeviceUseRecordController {

    @Resource
    private DeviceUseRecordService deviceUseRecordService;

    @GetMapping("/{uuid}")
    public Result<DeviceUseRecordVO> getByUuid(@PathVariable String uuid) {
        return deviceUseRecordService.getByUuid(uuid);
    }

    @GetMapping("/device/{deviceUuid}")
    public Result<List<DeviceUseRecordVO>> listByDevice(@PathVariable String deviceUuid) {
        return deviceUseRecordService.listByDeviceUuid(deviceUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DeviceUseRecordDTO dto) {
        return deviceUseRecordService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody DeviceUseRecordDTO dto) {
        return deviceUseRecordService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return deviceUseRecordService.deleteByUuid(uuid);
    }
}
