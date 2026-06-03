package com.geo.dsp.module.privilege.mapper;

import com.geo.dsp.module.privilege.entity.OperatorManage;
import com.geo.dsp.module.privilege.vo.OperatorManageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper
public interface OperatorManageMapper {

    int insert(OperatorManage operatorManage);

    OperatorManageVO selectByUuid(@Param("uuid") String uuid);

    List<OperatorManageVO> selectPage(
            @Param("operatorName") String operatorName,
            @Param("status") Integer status,
            @Param("startTime") OffsetDateTime startTime,
            @Param("endTime") OffsetDateTime endTime,
            @Param("offset") int offset,
            @Param("size") int size
    );

    long selectCount(
            @Param("operatorName") String operatorName,
            @Param("status") Integer status,
            @Param("startTime") OffsetDateTime startTime,
            @Param("endTime") OffsetDateTime endTime
    );

    int updateByUuid(OperatorManage operatorManage);

    int deleteByUuid(@Param("uuid") String uuid, @Param("updateBy") Long updateBy);

    int updateStatus(@Param("uuid") String uuid, @Param("status") Integer status, @Param("updateBy") Long updateBy);
}
