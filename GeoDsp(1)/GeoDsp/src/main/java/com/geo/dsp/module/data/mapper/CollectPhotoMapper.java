package com.geo.dsp.module.data.mapper;

import com.geo.dsp.module.data.entity.CollectPhoto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 照片表Mapper接口
 * 表说明：采集现场照片存储
 */
@Mapper
public interface CollectPhotoMapper {
    /**
     * 根据UUID查询照片
     * @param uuid 照片UUID
     * @return 照片实体
     */
    CollectPhoto selectByUuid(@Param("uuid") String uuid);

    /**
     * 根据采样数据UUID查询照片列表
     * @param dataUuid 采样数据UUID
     * @return 照片列表（未删除）
     */
    List<CollectPhoto> listByDataUuid(@Param("dataUuid") String dataUuid);

    /**
     * 新增照片
     * @param collectPhoto 照片实体（含UUID）
     * @return 受影响行数
     */
    int insert(CollectPhoto collectPhoto);

    /**
     * 根据UUID更新照片备注
     * @param uuid 照片UUID
     * @param updateBy 修改人ID
     * @return 受影响行数
     */
    int updateNoteByUuid(@Param("uuid") String uuid,@Param("updateBy") Long updateBy);

    /**
     * 根据采样数据UUID批量删除照片（逻辑删除）
     * @param dataUuid 采样数据UUID
     * @param updateBy 修改人ID
     * @return 受影响行数
     */
    int batchDeleteByDataUuid(@Param("dataUuid") String dataUuid, @Param("updateBy") Long updateBy);

    /**
     * 根据UUID逻辑删除照片
     * @param uuid 照片UUID
     * @param updateBy 修改人ID
     * @return 受影响行数
     */
    int deleteByUuid(@Param("uuid") String uuid, @Param("updateBy") Long updateBy);
}