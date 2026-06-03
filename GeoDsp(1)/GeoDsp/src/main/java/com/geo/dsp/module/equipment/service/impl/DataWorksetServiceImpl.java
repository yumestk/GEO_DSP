package com.geo.dsp.module.equipment.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.equipment.dto.DataWorksetDTO;
import com.geo.dsp.module.equipment.entity.DataWorkset;
import com.geo.dsp.module.equipment.mapper.DataWorksetMapper;
import com.geo.dsp.module.equipment.service.DataWorksetService;
import com.geo.dsp.module.equipment.vo.DataWorksetVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataWorksetServiceImpl implements DataWorksetService {

    @Resource
    private DataWorksetMapper dataWorksetMapper;

    private DataWorksetVO toVO(DataWorkset e) {
        if (e == null) return null;
        DataWorksetVO vo = new DataWorksetVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<PageVo<DataWorksetVO>> pageList(String keyword, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<DataWorksetVO> list = dataWorksetMapper.selectPage(keyword, offset, pageSize)
                .stream().map(this::toVO).collect(Collectors.toList());
        long total = dataWorksetMapper.selectCount(keyword);
        PageVo<DataWorksetVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    public Result<DataWorksetVO> getByUuid(String uuid) {
        return Result.success(toVO(dataWorksetMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<Boolean> create(DataWorksetDTO dto) {
        DataWorkset entity = new DataWorkset();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setWorkConfig(dto.getWorkConfig());
        entity.setSendcoilLen(dto.getSendcoilLen());
        entity.setSendcoilWidth(dto.getSendcoilWidth());
        entity.setSendcoilTurns(dto.getSendcoilTurns());
        entity.setRecvcoilSize(dto.getRecvcoilSize());
        entity.setRecvcoilGain(dto.getRecvcoilGain());
        entity.setNote(dto.getNote());
        entity.setIsDel(false);
        return dataWorksetMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增工作参数失败");
    }

    @Override
    public Result<Boolean> update(String uuid, DataWorksetDTO dto) {
        DataWorkset entity = dataWorksetMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("工作参数不存在");
        if (dto.getWorkConfig() != null) entity.setWorkConfig(dto.getWorkConfig());
        if (dto.getSendcoilLen() != null) entity.setSendcoilLen(dto.getSendcoilLen());
        if (dto.getSendcoilWidth() != null) entity.setSendcoilWidth(dto.getSendcoilWidth());
        if (dto.getSendcoilTurns() != null) entity.setSendcoilTurns(dto.getSendcoilTurns());
        if (dto.getRecvcoilSize() != null) entity.setRecvcoilSize(dto.getRecvcoilSize());
        if (dto.getRecvcoilGain() != null) entity.setRecvcoilGain(dto.getRecvcoilGain());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        return dataWorksetMapper.update(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid) {
        return dataWorksetMapper.deleteByUuid(uuid) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
