package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.dto.ProjectDTO;
import com.geo.dsp.module.data.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    Result<PageVo<ProjectVO>> pageList(String keyword, Long companyId, java.time.OffsetDateTime startTime, java.time.OffsetDateTime endTime, Integer status, int pageNum, int pageSize);
    Result<ProjectVO> getByUuid(String uuid);
    Result<List<ProjectVO>> listByCompanyUuid(String companyUuid);
    Result<Boolean> create(ProjectDTO dto);
    Result<Boolean> update(String uuid, ProjectDTO dto);
    Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
