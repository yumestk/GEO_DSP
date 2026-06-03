package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.entity.DataHistory;
import com.geo.dsp.module.data.mapper.DataHistoryMapper;
import com.geo.dsp.module.data.service.DataHistoryService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataHistoryServiceImpl implements DataHistoryService {

    @Resource
    private DataHistoryMapper dataHistoryMapper;

    @Override
    public Result<DataHistory> getByUuid(String uuid) {
        return Result.success(dataHistoryMapper.selectByUuid(uuid));
    }

    @Override
    public Result<List<DataHistory>> listByDataUuid(String dataUuid) {
        return Result.success(dataHistoryMapper.listByDataUuid(dataUuid));
    }

    @Override
    public Result<Boolean> create(DataHistory dataHistory) {
        if (dataHistory.getUuid() == null) {
            dataHistory.setUuid(UuidUtil.generateUuid());
        }
        dataHistory.setIsDel(false);
        return dataHistoryMapper.insert(dataHistory) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return dataHistoryMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
