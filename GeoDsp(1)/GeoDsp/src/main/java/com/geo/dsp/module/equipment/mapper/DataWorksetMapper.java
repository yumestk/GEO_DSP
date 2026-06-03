package com.geo.dsp.module.equipment.mapper;

import com.geo.dsp.module.equipment.entity.DataWorkset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备工作参数表Mapper接口
 * 对应表：data_workset
 * 功能：设备工作参数的增删改查、UUID专属操作
 */
@Mapper
public interface DataWorksetMapper {

    /**
     * 根据ID查询设备工作参数
     *
     * @param id  工作参数主键ID
     * @return DataWorkset  工作参数实体
     */
    DataWorkset selectById(@Param("id") Long id);

    /**
     * 新增设备工作参数
     *
     * @param dataWorkset 工作参数实体
     * @return int  受影响行数
     */
    int insert(DataWorkset dataWorkset);

    /**
     * 修改设备工作参数
     *
     * @param dataWorkset 工作参数实体（含更新字段）
     * @return int  受影响行数
     */
    int update(DataWorkset dataWorkset);

    /**
     * 根据ID删除设备工作参数（逻辑删除）
     *
     * @param id 工作参数主键ID
     * @return int  受影响行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 根据UUID查询设备工作参数
     *
     * @param uuid 工作参数唯一标识
     * @return DataWorkset  工作参数实体
     */
    DataWorkset selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据UUID删除设备工作参数（逻辑删除）
     *
     * @param uuid 工作参数唯一标识
     * @return int  受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid);

    List<DataWorkset> selectPage(@Param("keyword") String keyword, @Param("offset") int offset, @Param("size") int size);

    long selectCount(@Param("keyword") String keyword);
}