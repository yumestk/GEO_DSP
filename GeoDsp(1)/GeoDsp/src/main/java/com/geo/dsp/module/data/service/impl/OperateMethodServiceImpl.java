package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.module.data.dto.OperateMethodDTO;
import com.geo.dsp.module.data.entity.OperateMethod;
import com.geo.dsp.module.data.mapper.OperateMethodMapper;
import com.geo.dsp.module.data.service.OperateMethodService;
import com.geo.dsp.module.data.vo.OperateMethodVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperateMethodServiceImpl implements OperateMethodService {

    @Resource
    private OperateMethodMapper operateMethodMapper;

    private OperateMethodVO toVO(OperateMethod e) {
        if (e == null) return null;
        OperateMethodVO vo = new OperateMethodVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<List<OperateMethodVO>> listAll() {
        return Result.success(operateMethodMapper.listAll().stream().map(this::toVO).collect(Collectors.toList()));
    }

    @Override
    public Result<OperateMethodVO> getByUuid(String uuid) {
        return Result.success(toVO(operateMethodMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<Boolean> create(OperateMethodDTO dto) {
        OperateMethod entity = new OperateMethod();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setMethodName(dto.getMethodName());
        entity.setMethodCode(dto.getMethodCode());
        entity.setMethodDesc(dto.getMethodDesc());
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return operateMethodMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> update(String uuid, OperateMethodDTO dto) {
        OperateMethod entity = operateMethodMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("作业方法不存在");
        if (dto.getMethodName() != null) entity.setMethodName(dto.getMethodName());
        if (dto.getMethodDesc() != null) entity.setMethodDesc(dto.getMethodDesc());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return operateMethodMapper.updateByUuid(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return operateMethodMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
