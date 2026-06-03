package com.geo.dsp.module.equipment.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.equipment.dto.DeviceDTO;
import com.geo.dsp.module.equipment.service.DeviceService;
import com.geo.dsp.module.equipment.vo.DeviceVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @GetMapping("/list")
    public Result<PageVo<DeviceVO>> list(
            @RequestParam(required = false) String deviceNum,
            @RequestParam(required = false) Long deviceTypeId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return deviceService.pageList(deviceNum, deviceTypeId, status, pageNum, pageSize);
    }

    @GetMapping("/{uuid}")
    public Result<DeviceVO> getByUuid(@PathVariable String uuid) {
        return deviceService.getByUuid(uuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody DeviceDTO dto) {
        return deviceService.create(dto);
    }

    @PutMapping("/{uuid}")
    public Result<Boolean> update(@PathVariable String uuid, @RequestBody DeviceDTO dto) {
        return deviceService.update(uuid, dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return deviceService.deleteByUuid(uuid, updateBy);
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> updateStatus(@PathVariable String uuid,
                                        @RequestParam Integer status,
                                        @RequestParam(defaultValue = "1") Long updateBy) {
        return deviceService.updateStatus(uuid, status, updateBy);
    }

    @PostMapping("/batch")
    public Result<Boolean> batchOperation(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> uuids = (List<String>) body.get("uuids");
        String action = (String) body.get("action");
        Object updateByObj = body.get("updateBy");
        Long updateBy = updateByObj != null ? ((Number) updateByObj).longValue() : 1L;
        return deviceService.batchOperation(uuids, action, updateBy);
    }
}
