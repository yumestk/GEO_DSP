package com.geo.dsp.module.equipment.mapper;

import com.geo.dsp.module.equipment.entity.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备表Mapper接口
 * 对应表：device
 * 功能：设备信息的增删改查、UUID专属操作
 */
@Mapper
public interface DeviceMapper {

    /**
     * 根据ID查询设备信息
     *
     * @param id  设备主键ID
     * @return Device  设备信息实体
     */
    Device selectById(@Param("id") Long id);

    /**
     * 根据公司ID查询设备列表
     *
     * @param companyId 公司主键ID
     * @return List<Device> 符合条件的设备列表
     */
    List<Device> listByCompanyId(@Param("companyId") Long companyId);

    /**
     * 根据状态查询设备列表
     *
     * @param status 设备状态（0-禁用，1-启用，2-维修中）
     * @return List<Device> 符合条件的设备列表
     */
    List<Device> listByStatus(@Param("status") Integer status);

    /**
     * 根据设备编号查询设备信息
     *
     * @param deviceNum 设备编号
     * @return Device  设备信息实体
     */
    Device selectByDeviceNum(@Param("deviceNum") String deviceNum);

    /**
     * 新增设备信息
     *
     * @param device 设备信息实体
     * @return int  受影响行数
     */
    int insert(Device device);

    /**
     * 修改设备信息
     *
     * @param device 设备信息实体（含更新字段）
     * @return int  受影响行数
     */
    int update(Device device);

    /**
     * 修改设备状态
     *
     * @param id     设备主键ID
     * @param status 目标状态（0-禁用，1-启用，2-维修中）
     * @return int  受影响行数
     */
    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 根据ID删除设备（逻辑删除）
     *
     * @param id 设备主键ID
     * @return int  受影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据UUID查询设备信息
     *
     * @param uuid 设备唯一标识
     * @return Device  设备信息实体
     */
    Device selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据UUID修改设备状态
     *
     * @param uuid   设备唯一标识
     * @param status 目标状态（0-禁用，1-启用，2-维修中）
     * @return int  受影响行数
     */
    int updateStatusByUuid(@Param("uuid") String uuid, @Param("status") Integer status);

    /**
     * 根据UUID删除设备（逻辑删除）
     *
     * @param uuid 设备唯一标识
     * @return int  受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);

    /**
     * 根据公司UUID查询设备列表
     *
     * @param companyUuid 公司唯一标识
     * @return List<Device> 符合条件的设备列表
     */
    List<Device> listByCompanyUuid(@Param("companyUuid") String companyUuid);

    /**
     * 分页查询设备列表
     */
    List<Device> selectPage(
            @Param("deviceNum") String deviceNum,
            @Param("deviceTypeId") Long deviceTypeId,
            @Param("status") Integer status,
            @Param("offset") int offset,
            @Param("size") int size
    );

    /**
     * 查询设备总数
     */
    long selectCount(
            @Param("deviceNum") String deviceNum,
            @Param("deviceTypeId") Long deviceTypeId,
            @Param("status") Integer status
    );
}