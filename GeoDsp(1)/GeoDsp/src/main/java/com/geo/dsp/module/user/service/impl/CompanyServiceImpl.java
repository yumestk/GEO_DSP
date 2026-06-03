package com.geo.dsp.module.user.service.impl;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.user.dto.CompanyAddDTO;
import com.geo.dsp.module.user.dto.CompanyUpdateDTO;
import com.geo.dsp.module.user.entity.Company;
import com.geo.dsp.module.user.mapper.CompanyMapper;
import com.geo.dsp.module.user.service.CompanyService;
import com.geo.dsp.module.user.vo.CompanyVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Override
    public Result<PageVo<CompanyVO>> getPage(String keyword, Integer pageNum, Integer pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<CompanyVO> list = companyMapper.selectCompanyPage(keyword, offset, pageSize);
        long total = companyMapper.selectCompanyCount(keyword);

        PageVo<CompanyVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(CompanyAddDTO dto, Long createBy) {
        Company company = new Company();
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyAbbr(dto.getCompanyAbbr());
        company.setContactName(dto.getContactName());
        company.setContactPhone(dto.getContactPhone());
        company.setContactEmail(dto.getContactEmail());
        company.setAddress(dto.getAddress());
        company.setLicenseNum(dto.getLicenseNum());
        company.setNote(dto.getNote());
        company.setStatus(dto.getStatus());
        company.setCreateBy(createBy);
        company.setUpdateBy(createBy);
        company.setIsDel(false);

        return companyMapper.insertCompany(company) > 0
                ? Result.success(true)
                : Result.fail("新增企业失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(CompanyUpdateDTO dto, Long updateBy) {
        Company company = new Company();
        company.setUuid(dto.getUuid());
        company.setCompanyName(dto.getCompanyName());
        company.setCompanyAbbr(dto.getCompanyAbbr());
        company.setContactName(dto.getContactName());
        company.setContactPhone(dto.getContactPhone());
        company.setContactEmail(dto.getContactEmail());
        company.setAddress(dto.getAddress());
        company.setLicenseNum(dto.getLicenseNum());
        company.setNote(dto.getNote());
        company.setStatus(dto.getStatus());
        company.setUpdateBy(updateBy);

        return companyMapper.updateCompany(company) > 0
                ? Result.success(true)
                : Result.fail("修改失败，企业不存在");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(String uuid) {
        return companyMapper.deleteByUuid(uuid) > 0
                ? Result.success(true)
                : Result.fail("删除失败，企业不存在");
    }
}