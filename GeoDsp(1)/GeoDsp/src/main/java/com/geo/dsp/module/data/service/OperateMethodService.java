package com.geo.dsp.module.data.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.data.dto.OperateMethodDTO;
import com.geo.dsp.module.data.vo.OperateMethodVO;

import java.util.List;

public interface OperateMethodService {
    Result<List<OperateMethodVO>> listAll();
    Result<OperateMethodVO> getByUuid(String uuid);
    Result<Boolean> create(OperateMethodDTO dto);
    Result<Boolean> update(String uuid, OperateMethodDTO dto);
    Result<Boolean> deleteByUuid(String uuid, Long updateBy);
}
