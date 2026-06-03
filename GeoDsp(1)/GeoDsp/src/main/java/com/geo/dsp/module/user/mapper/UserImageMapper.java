package com.geo.dsp.module.user.mapper;

import com.geo.dsp.module.user.entity.UserImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户图片表Mapper接口
 * 对应表：user_image
 * 功能：用户图片信息的增删改查、UUID专属操作

 */
@Mapper
public interface UserImageMapper {

    /**
     * 根据ID查询用户图片信息
     *
     * @param id  用户图片主键ID
     * @return UserImage  用户图片信息实体
     */
    UserImage selectById(@Param("id") Long id);

    /**
     * 新增用户图片信息
     *
     * @param userImage 用户图片信息实体
     * @return int  受影响行数
     */
    int insert(UserImage userImage);

    /**
     * 根据ID删除用户图片（逻辑删除）
     *
     * @param id 用户图片主键ID
     * @return int  受影响行数
     */
    int deleteById(@Param("id") Long id);

}