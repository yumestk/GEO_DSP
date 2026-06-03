package com.geo.dsp.module.data.mapper;

import com.geo.dsp.module.data.entity.OperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作业日志表Mapper接口
 * 表说明：每日作业情况记录，关联项目、工程和采集任务
 */
@Mapper
public interface OperationLogMapper {
    OperationLog selectByUuid(@Param("uuid") String uuid);
    List<OperationLog> listByProjectUuid(@Param("projectUuid") String projectUuid);
    List<OperationLog> listByTaskUuid(@Param("taskUuid") String taskUuid);
    List<OperationLog> listByLogDate(@Param("logDate") String logDate, @Param("creatorId") Long creatorId);
    List<OperationLog> selectPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);
    long selectCount(@Param("keyword") String keyword);
    int insert(OperationLog operationLog);
    int updateContentByUuid(@Param("uuid") String uuid, @Param("content") String content, @Param("updateBy") Long updateBy);
    int deleteByUuid(@Param("uuid") String uuid, @Param("updateBy") Long updateBy);
}