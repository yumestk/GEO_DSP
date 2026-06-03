package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.dto.CollectTaskDTO;
import com.geo.dsp.module.data.entity.CollectTask;
import com.geo.dsp.module.data.mapper.CollectTaskMapper;
import com.geo.dsp.module.data.service.CollectTaskService;
import com.geo.dsp.module.data.vo.CollectTaskVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CollectTaskServiceImpl implements CollectTaskService {

    @Resource
    private CollectTaskMapper collectTaskMapper;

    private CollectTaskVO toVO(CollectTask e) {
        if (e == null) return null;
        CollectTaskVO vo = new CollectTaskVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<PageVo<CollectTaskVO>> pageList(String keyword, String projectUuid, Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<CollectTaskVO> list = collectTaskMapper.selectPage(keyword, projectUuid, status, offset, pageSize)
                .stream().map(this::toVO).collect(Collectors.toList());
        long total = collectTaskMapper.selectCount(keyword, projectUuid, status);
        PageVo<CollectTaskVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    public Result<CollectTaskVO> getByUuid(String uuid) {
        return Result.success(toVO(collectTaskMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<CollectTaskVO>> listByProjectUuid(String projectUuid) {
        List<CollectTaskVO> list = collectTaskMapper.listByProjectUuid(projectUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<Boolean> create(CollectTaskDTO dto) {
        CollectTask entity = new CollectTask();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setProjectId(dto.getProjectId());
        entity.setTaskName(dto.getTaskName());
        entity.setTaskDesc(dto.getTaskDesc());
        entity.setExecutorId(dto.getExecutorId());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setMethodId(dto.getMethodId());
        entity.setDeviceId(dto.getDeviceId());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return collectTaskMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增任务失败");
    }

    @Override
    public Result<Boolean> update(String uuid, CollectTaskDTO dto) {
        CollectTask entity = collectTaskMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("任务不存在");
        if (dto.getTaskName() != null) entity.setTaskName(dto.getTaskName());
        if (dto.getEndTime() != null) entity.setEndTime(dto.getEndTime());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return collectTaskMapper.updateByUuid(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy) {
        return collectTaskMapper.updateStatusByUuid(uuid, status, updateBy) > 0
                ? Result.success(true) : Result.fail("状态更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return collectTaskMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
