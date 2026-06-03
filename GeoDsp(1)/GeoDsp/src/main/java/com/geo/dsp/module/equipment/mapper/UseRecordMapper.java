package com.geo.dsp.module.equipment.mapper;

import com.geo.dsp.module.equipment.entity.UseRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备使用记录表Mapper接口
 * 对应表：use_record
 * 功能：设备使用记录的增删改查、UUID专属操作
 */
@Mapper
public interface UseRecordMapper {

    /**
     * 根据设备ID查询使用记录列表
     *
     * @param deviceId 设备主键ID
     * @return List<UseRecord> 符合条件的使用记录列表
     */
    List<UseRecord> listByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 根据用户ID查询使用记录列表
     *
     * @param userId 用户主键ID
     * @return List<UseRecord> 符合条件的使用记录列表
     */
    List<UseRecord> listByUserId(@Param("userId") Long userId);

    /**
     * 新增设备使用记录
     *
     * @param useRecord 使用记录实体
     * @return int  受影响行数
     */
    int insert(UseRecord useRecord);

    /**
     * 修改设备使用记录
     *
     * @param useRecord 使用记录实体（含更新字段）
     * @return int  受影响行数
     */
    int update(UseRecord useRecord);

    /**
     * 根据ID删除设备使用记录（逻辑删除）
     *
     * @param id 使用记录主键ID
     * @return int  受影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据UUID查询设备使用记录
     *
     * @param uuid 使用记录唯一标识
     * @return UseRecord  使用记录实体
     */
    UseRecord selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据UUID删除设备使用记录（逻辑删除）
     *
     * @param uuid 使用记录唯一标识
     * @return int  受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);

    /**
     * 根据设备UUID查询使用记录列表
     *
     * @param deviceUuid 设备唯一标识
     * @return List<UseRecord> 符合条件的使用记录列表
     */
    List<UseRecord> listByDeviceUuid(@Param("deviceUuid") String deviceUuid);
}