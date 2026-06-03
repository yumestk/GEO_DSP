package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.dto.ProjectDTO;
import com.geo.dsp.module.data.entity.Project;
import com.geo.dsp.module.data.mapper.ProjectMapper;
import com.geo.dsp.module.data.service.ProjectService;
import com.geo.dsp.module.data.vo.ProjectVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Resource
    private ProjectMapper projectMapper;

    private ProjectVO toVO(Project e) {
        if (e == null) return null;
        ProjectVO vo = new ProjectVO();
        BeanUtils.copyProperties(e, vo);
        return vo;
    }

    @Override
    public Result<PageVo<ProjectVO>> pageList(String keyword, Long companyId, OffsetDateTime startTime, OffsetDateTime endTime, Integer status, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<ProjectVO> list = projectMapper.selectPage(keyword, companyId, startTime, endTime, status, offset, pageSize)
                .stream().map(this::toVO).collect(Collectors.toList());
        long total = projectMapper.selectCount(keyword, companyId, startTime, endTime, status);
        PageVo<ProjectVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    public Result<ProjectVO> getByUuid(String uuid) {
        return Result.success(toVO(projectMapper.selectByUuid(uuid)));
    }

    @Override
    public Result<List<ProjectVO>> listByCompanyUuid(String companyUuid) {
        List<ProjectVO> list = projectMapper.listByCompanyUuid(companyUuid)
                .stream().map(this::toVO).collect(Collectors.toList());
        return Result.success(list);
    }

    @Override
    public Result<Boolean> create(ProjectDTO dto) {
        Project entity = new Project();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setCompanyId(dto.getCompanyId());
        entity.setProjectName(dto.getProjectName());
        entity.setProjectCode(dto.getProjectCode());
        entity.setProjectAddress(dto.getProjectAddress());
        entity.setStartTime(dto.getStartTime());
        entity.setEndTime(dto.getEndTime());
        entity.setManagerId(dto.getManagerId());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 0);
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return projectMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增项目失败");
    }

    @Override
    public Result<Boolean> update(String uuid, ProjectDTO dto) {
        Project entity = projectMapper.selectByUuid(uuid);
        if (entity == null) return Result.fail("项目不存在");
        if (dto.getProjectName() != null) entity.setProjectName(dto.getProjectName());
        if (dto.getProjectAddress() != null) entity.setProjectAddress(dto.getProjectAddress());
        if (dto.getEndTime() != null) entity.setEndTime(dto.getEndTime());
        if (dto.getStatus() != null) entity.setStatus(dto.getStatus());
        if (dto.getNote() != null) entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        entity.setUpdateTime(OffsetDateTime.now());
        return projectMapper.updateByUuid(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy) {
        return projectMapper.updateStatusByUuid(uuid, status, updateBy) > 0
                ? Result.success(true) : Result.fail("状态更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return projectMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
