package com.geo.dsp.module.privilege.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.privilege.dto.OperatorManageDTO;
import com.geo.dsp.module.privilege.vo.OperatorManageVO;

import java.time.OffsetDateTime;
import java.util.List;

public interface OperatorManageService {

    Result<PageVo<OperatorManageVO>> pageList(String operatorName, Integer status,
                                              OffsetDateTime startTime, OffsetDateTime endTime,
                                              int pageNum, int pageSize);

    Result<OperatorManageVO> getByUuid(String uuid);

    Result<Boolean> create(OperatorManageDTO dto);

    Result<Boolean> update(String uuid, OperatorManageDTO dto);

    Result<Boolean> deleteByUuid(String uuid, Long updateBy);

    Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy);

    Result<Boolean> batchOperation(java.util.List<String> uuids, String action, Long updateBy);

    byte[] exportTemplate();

    int importFromFile(byte[] fileContent, Long operateBy);

    int importFromSystem(List<OperatorManageDTO> dtoList, Long operateBy);
}
