package com.geo.dsp.module.user.service;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.user.dto.CompanyAddDTO;
import com.geo.dsp.module.user.dto.CompanyUpdateDTO;
import com.geo.dsp.module.user.entity.Company;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.user.vo.CompanyVO;

import java.util.List;

/**
 * 企业服务层接口
 * 定义企业相关的业务方法
 */
public interface CompanyService {

    Result<PageVo<CompanyVO>> getPage(String keyword, Integer pageNum, Integer pageSize);
    Result<Boolean> add(CompanyAddDTO dto, Long createBy);
    Result<Boolean> update(CompanyUpdateDTO dto, Long updateBy);
    Result<Boolean> delete(String uuid);
}