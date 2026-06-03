package com.geo.dsp.module.data.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.entity.CollectPhoto;
import com.geo.dsp.module.data.service.CollectPhotoService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/collect-photo")
public class CollectPhotoController {

    @Resource
    private CollectPhotoService collectPhotoService;

    @GetMapping("/{uuid}")
    public Result<CollectPhoto> getByUuid(@PathVariable String uuid) {
        return collectPhotoService.getByUuid(uuid);
    }

    @GetMapping("/data/{dataUuid}")
    public Result<List<CollectPhoto>> listByData(@PathVariable String dataUuid) {
        return collectPhotoService.listByDataUuid(dataUuid);
    }

    @PostMapping
    public Result<Boolean> create(@RequestBody CollectPhoto collectPhoto) {
        return collectPhotoService.create(collectPhoto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        return collectPhotoService.deleteByUuid(uuid, updateBy);
    }
}
