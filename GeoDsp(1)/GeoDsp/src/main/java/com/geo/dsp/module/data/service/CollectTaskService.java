package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.dto.CollectTaskDTO;
import com.geo.dsp.module.data.vo.CollectTaskVO;

import java.util.List;

public interface CollectTaskService {
    Result<PageVo<CollectTaskVO>> pageList(String keyword, String projectUuid, Integer status, int pageNum, int pageSize);
    Result<CollectTaskVO> getByUuid(String uuid);
    Result<List<CollectTaskVO>> listByProjectUuid(String projectUuid);
    Result<Boolean> create(CollectTaskDTO dto);
    Result<Boolean> update(String uuid, CollectTaskDTO dto);
    Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
