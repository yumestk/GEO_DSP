package com.geo.dsp.module.login.mapper;

import com.geo.dsp.module.login.entity.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志表Mapper接口
 * 对应表：operate_log
 * 功能：操作日志的新增、查询、UUID专属操作
 */
@Mapper
public interface OperateLogMapper {
    /**
     * 根据UUID查询操作日志
     * @param uuid 日志对外主键（32位无横线UUID）
     * @return OperateLog 操作日志实体（含变更前后JSON数据、操作人/企业关联信息）
     */
    OperateLog selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据用户UUID查询操作日志
     * @param userUuid 操作人UUID（关联users表uuid）
     * @return List<OperateLog> 该用户的所有操作日志（按操作时间倒序排列）
     */
    List<OperateLog> listByUserUuid(@Param("userUuid") String userUuid);

    /**
     * 根据模块名称查询操作日志
     * @param moduleName 操作模块名称（如：设备管理、项目管理，对应表module_name字段）
     * @return List<OperateLog> 该模块的所有操作日志（按操作时间倒序排列）
     */
    List<OperateLog> listByModuleName(@Param("moduleName") String moduleName);

    /**
     * 根据企业UUID查询操作日志
     * @param companyUuid 所属企业UUID（关联companies表uuid，多租户隔离核心参数）
     * @return List<OperateLog> 该企业下的所有操作日志（按操作时间倒序排列）
     */
    List<OperateLog> listByCompanyUuid(@Param("companyUuid") String companyUuid);

    /**
     * 新增操作日志
     * @param operateLog 操作日志实体（需含uuid、user_id、company_id、module_name等必填字段，operate_record为JSON格式）
     * @return int 受影响行数（1=新增成功，0=新增失败）
     */
    int insert(OperateLog operateLog);

    /**
     * 根据UUID更新操作日志
     * @param operateLog 操作日志实体（含uuid，支持更新operate_desc、new_record等非空字段）
     * @return int 受影响行数（1=更新成功，0=无字段变更或日志不存在）
     */
    int updateByUuid(OperateLog operateLog);

    /**
     * 根据UUID逻辑删除操作日志
     * @param uuid 日志UUID（对外主键）
     * @param updateBy 修改人ID（关联users表id，操作溯源用）
     * @return int 受影响行数（1=删除成功，0=日志不存在）
     */
    int deleteByUuid(@Param("uuid") String uuid, @Param("updateBy") Long updateBy);
}