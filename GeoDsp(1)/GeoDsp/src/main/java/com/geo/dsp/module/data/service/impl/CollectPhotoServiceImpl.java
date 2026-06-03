package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.entity.CollectPhoto;
import com.geo.dsp.module.data.mapper.CollectPhotoMapper;
import com.geo.dsp.module.data.service.CollectPhotoService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class CollectPhotoServiceImpl implements CollectPhotoService {

    @Resource
    private CollectPhotoMapper collectPhotoMapper;

    @Override
    public Result<CollectPhoto> getByUuid(String uuid) {
        return Result.success(collectPhotoMapper.selectByUuid(uuid));
    }

    @Override
    public Result<List<CollectPhoto>> listByDataUuid(String dataUuid) {
        return Result.success(collectPhotoMapper.listByDataUuid(dataUuid));
    }

    @Override
    public Result<Boolean> create(CollectPhoto collectPhoto) {
        if (collectPhoto.getUuid() == null) {
            collectPhoto.setUuid(UuidUtil.generateUuid());
        }
        collectPhoto.setCreateTime(OffsetDateTime.now());
        collectPhoto.setIsDel(false);
        return collectPhotoMapper.insert(collectPhoto) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return collectPhotoMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
