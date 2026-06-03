package com.geo.dsp.module.equipment.mapper;

import com.geo.dsp.module.equipment.entity.DeviceLimit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备使用限制表Mapper接口
 * 对应表：device_limit
 * 功能：设备使用限制的增删改查、UUID专属操作
 */
@Mapper
public interface DeviceLimitMapper {

    /**
     * 根据设备ID查询使用限制列表
     *
     * @param deviceId 设备主键ID
     * @return List<DeviceLimit> 符合条件的使用限制列表
     */
    List<DeviceLimit> listByDeviceId(@Param("deviceId") Long deviceId);

    /**
     * 根据ID查询设备使用限制
     *
     * @param id  使用限制主键ID
     * @return DeviceLimit  使用限制实体
     */
    DeviceLimit selectById(@Param("id") Long id);

    /**
     * 新增设备使用限制
     *
     * @param deviceLimit 使用限制实体
     * @return int  受影响行数
     */
    int insert(DeviceLimit deviceLimit);

    /**
     * 修改设备使用限制
     *
     * @param deviceLimit 使用限制实体（含更新字段）
     * @return int  受影响行数
     */
    int update(DeviceLimit deviceLimit);

    /**
     * 根据ID删除设备使用限制（逻辑删除）
     *
     * @param id 使用限制主键ID
     * @return int  受影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据UUID查询设备使用限制
     *
     * @param uuid 使用限制唯一标识
     * @return DeviceLimit  使用限制实体
     */
    DeviceLimit selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据UUID删除设备使用限制（逻辑删除）
     *
     * @param uuid 使用限制唯一标识
     * @return int  受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);

    /**
     * 根据设备UUID查询使用限制列表
     *
     * @param deviceUuid 设备唯一标识
     * @return List<DeviceLimit> 符合条件的使用限制列表
     */
    List<DeviceLimit> listByDeviceUuid(@Param("deviceUuid") String deviceUuid);
}