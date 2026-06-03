package com.geo.dsp.module.login.mapper;

import com.geo.dsp.module.login.entity.LoginLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登录日志表Mapper接口
 * 对应表：login_log
 * 功能：登录日志的新增、查询、UUID专属操作
 */
@Mapper
public interface LoginLogMapper {

    /**
     * 新增登录日志
     *
     * @param loginLog 登录日志实体
     * @return int  受影响行数
     */
    int insert(LoginLog loginLog);

    /**
     * 根据用户ID查询登录日志列表
     *
     * @param userId 用户主键ID
     * @return List<LoginLog> 符合条件的登录日志列表
     */
    List<LoginLog> listByUserId(@Param("userId") Long userId);

    /**
     * 根据时间范围查询登录日志列表
     *
     * @param startTime 开始时间（格式：yyyy-MM-dd HH:mm:ss）
     * @param endTime   结束时间（格式：yyyy-MM-dd HH:mm:ss）
     * @return List<LoginLog> 符合条件的登录日志列表
     */
    List<LoginLog> listByTimeRange(@Param("startTime") String startTime, @Param("endTime") String endTime);

    /**
     * 根据UUID查询登录日志
     *
     * @param uuid 登录日志唯一标识
     * @return LoginLog  登录日志实体
     */
    LoginLog selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据用户UUID查询登录日志列表
     *
     * @param userUuid 用户唯一标识
     * @return List<LoginLog> 符合条件的登录日志列表
     */
    List<LoginLog> listByUserUuid(@Param("userUuid") String userUuid);

    /**
     * 根据UUID删除登录日志（逻辑删除）
     *
     * @param uuid 登录日志唯一标识
     * @return int  受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);
}