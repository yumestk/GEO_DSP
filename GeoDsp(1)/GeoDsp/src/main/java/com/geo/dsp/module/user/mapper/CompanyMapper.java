package com.geo.dsp.module.user.mapper;


import com.geo.dsp.module.user.entity.Company;
import com.geo.dsp.module.user.vo.CompanyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CompanyMapper {

    // 新增企业
    int insertCompany(Company company);

    // 分页列表
    List<CompanyVO> selectCompanyPage(
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("size") int size
    );

    // 总数
    long selectCompanyCount(@Param("keyword") String keyword);

    // 修改
    int updateCompany(Company company);

    // 逻辑删除
    int deleteByUuid(@Param("uuid") String uuid);
}