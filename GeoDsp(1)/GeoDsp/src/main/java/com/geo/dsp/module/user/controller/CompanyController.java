package com.geo.dsp.module.user.controller;

import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.common.result.Result;
import com.geo.dsp.module.user.dto.CompanyAddDTO;
import com.geo.dsp.module.user.dto.CompanyUpdateDTO;
import com.geo.dsp.module.user.service.CompanyService;
import com.geo.dsp.module.user.vo.CompanyVO;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/system/companies")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 企业列表 + 搜索
     */
    @GetMapping("/list")
    public Result<PageVo<CompanyVO>> list(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return companyService.getPage(keyword, pageNum, pageSize);
    }

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result<Boolean> add(
            @Valid @RequestBody CompanyAddDTO dto,
            @RequestParam(value = "createBy", required = false, defaultValue = "1") Long createBy
    ) {
        return companyService.add(dto, createBy);
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result<Boolean> update(
            @Valid @RequestBody CompanyUpdateDTO dto,
            @RequestParam(value = "updateBy", required = false, defaultValue = "1") Long updateBy
    ) {
        return companyService.update(dto, updateBy);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid) {
        return companyService.delete(uuid);
    }
}