package com.geo.dsp.module.data.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.data.entity.OperationLog;
import com.geo.dsp.module.data.mapper.OperationLogMapper;
import com.geo.dsp.module.data.service.OperationLogService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Override
    public Result<PageVo<OperationLog>> pageList(String keyword, int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<OperationLog> list = operationLogMapper.selectPage(keyword, offset, pageSize);
        long total = operationLogMapper.selectCount(keyword);
        PageVo<OperationLog> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    public Result<OperationLog> getByUuid(String uuid) {
        return Result.success(operationLogMapper.selectByUuid(uuid));
    }

    @Override
    public Result<List<OperationLog>> listByProjectUuid(String projectUuid) {
        return Result.success(operationLogMapper.listByProjectUuid(projectUuid));
    }

    @Override
    public Result<Boolean> create(OperationLog operationLog) {
        if (operationLog.getUuid() == null) {
            operationLog.setUuid(UuidUtil.generateUuid());
        }
        operationLog.setIsDel(false);
        return operationLogMapper.insert(operationLog) > 0 ? Result.success(true) : Result.fail("新增失败");
    }

    @Override
    public Result<Boolean> updateContent(String uuid, String content, Long updateBy) {
        return operationLogMapper.updateContentByUuid(uuid, content, updateBy) > 0
                ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return operationLogMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }
}
