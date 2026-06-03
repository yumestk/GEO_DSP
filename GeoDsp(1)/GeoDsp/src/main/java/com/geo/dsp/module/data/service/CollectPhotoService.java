package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.entity.CollectPhoto;

import java.util.List;

public interface CollectPhotoService {
    Result<CollectPhoto> getByUuid(String uuid);
    Result<List<CollectPhoto>> listByDataUuid(String dataUuid);
    Result<Boolean> create(CollectPhoto collectPhoto);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
