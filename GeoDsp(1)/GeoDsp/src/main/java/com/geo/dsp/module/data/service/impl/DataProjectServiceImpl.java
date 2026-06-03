package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.entity.DataProject;
import com.geo.dsp.module.data.mapper.DataProjectMapper;
import com.geo.dsp.module.data.service.DataProjectService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Service
public class DataProjectServiceImpl implements DataProjectService {

    @Resource
    private DataProjectMapper dataProjectMapper;

    @Override
    public Result<DataProject> getByUuid(String uuid) {
        return Result.success(dataProjectMapper.selectByUuid(uuid));
    }

    @Override
    public Result<List<DataProject>> listByProjectUuid(String projectUuid) {
        return Result.success(dataProjectMapper.listByProjectUuid(projectUuid));
    }

    @Override
    public Result<Boolean> create(DataProject dataProject) {
        if (dataProject.getUuid() == null) {
            dataProject.setUuid(UuidUtil.generateUuid());
        }
        LocalDateTime now = LocalDateTime.now();
        dataProject.setCreatetime(now);
        dataProject.setUpdateTime(OffsetDateTime.now());
        dataProject.setIsDel(false);
        return dataProjectMapper.insert(dataProject) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> update(DataProject dataProject) {
        dataProject.setUpdateTime(OffsetDateTime.now());
        return dataProjectMapper.updateByUuid(dataProject) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy) {
        return dataProjectMapper.updateStatusByUuid(uuid, status, updateBy) > 0
                ? Result.success(true) : Result.fail("状态更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return dataProjectMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
