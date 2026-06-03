package com.geo.dsp.module.equipment.mapper;

import com.geo.dsp.module.equipment.entity.DeviceType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备类型表Mapper接口
 * 对应表：device_type
 * 功能：设备类型信息的增删改查、UUID专属操作
 */
@Mapper
public interface DeviceTypeMapper {

    /**
     * 根据ID查询设备类型信息
     *
     * @param id  设备类型主键ID
     * @return DeviceType  设备类型信息实体
     */
    DeviceType selectById(@Param("id") Long id);

    /**
     * 查询所有设备类型列表
     *
     * @return List<DeviceType> 所有设备类型列表（未删除）
     */
    List<DeviceType> listAll();

    /**
     * 根据类型名称查询设备类型信息
     *
     * @param typeName 设备类型名称
     * @return DeviceType  设备类型信息实体
     */
    DeviceType selectByName(@Param("typeName") String typeName);

    /**
     * 新增设备类型信息
     *
     * @param deviceType 设备类型信息实体
     * @return int  受影响行数
     */
    int insert(DeviceType deviceType);

    /**
     * 修改设备类型信息
     *
     * @param deviceType 设备类型信息实体（含更新字段）
     * @return int  受影响行数
     */
    int update(DeviceType deviceType);

    /**
     * 根据ID删除设备类型（逻辑删除）
     *
     * @param id 设备类型主键ID
     * @return int  受影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据UUID查询设备类型信息
     *
     * @param uuid 设备类型唯一标识
     * @return DeviceType  设备类型信息实体
     */
    DeviceType selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据UUID删除设备类型（逻辑删除）
     *
     * @param uuid 设备类型唯一标识
     * @return int  受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);
}