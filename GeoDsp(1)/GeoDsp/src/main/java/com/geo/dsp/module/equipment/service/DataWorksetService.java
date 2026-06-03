package com.geo.dsp.module.equipment.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.equipment.dto.DataWorksetDTO;
import com.geo.dsp.module.equipment.vo.DataWorksetVO;

public interface DataWorksetService {
    Result<PageVo<DataWorksetVO>> pageList(String keyword, int pageNum, int pageSize);
    Result<DataWorksetVO> getByUuid(String uuid);
    Result<Boolean> create(DataWorksetDTO dto);
    Result<Boolean> update(String uuid, DataWorksetDTO dto);
    Result<Boolean> deleteByUuid(String uuid);
}
