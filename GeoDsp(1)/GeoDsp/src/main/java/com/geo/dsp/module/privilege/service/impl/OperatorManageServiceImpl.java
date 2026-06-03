package com.geo.dsp.module.privilege.service.impl;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.util.UuidUtil;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.privilege.dto.OperatorManageDTO;
import com.geo.dsp.module.privilege.entity.OperatorManage;
import com.geo.dsp.module.privilege.mapper.OperatorManageMapper;
import com.geo.dsp.module.privilege.service.OperatorManageService;
import com.geo.dsp.module.privilege.vo.OperatorManageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
public class OperatorManageServiceImpl implements OperatorManageService {

    @Resource
    private OperatorManageMapper operatorManageMapper;

    @Override
    public Result<PageVo<OperatorManageVO>> pageList(String operatorName, Integer status,
                                                     OffsetDateTime startTime, OffsetDateTime endTime,
                                                     int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        List<OperatorManageVO> list = operatorManageMapper.selectPage(operatorName, status, startTime, endTime, offset, pageSize);
        long total = operatorManageMapper.selectCount(operatorName, status, startTime, endTime);
        PageVo<OperatorManageVO> pageVo = new PageVo<>();
        pageVo.setRecords(list);
        pageVo.setTotal(total);
        pageVo.setPageNum(pageNum);
        pageVo.setPageSize(pageSize);
        return Result.success(pageVo);
    }

    @Override
    public Result<OperatorManageVO> getByUuid(String uuid) {
        return Result.success(operatorManageMapper.selectByUuid(uuid));
    }

    @Override
    public Result<Boolean> create(OperatorManageDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getOperatorName())) {
            return Result.fail("操作员姓名不能为空");
        }
        OperatorManage entity = new OperatorManage();
        entity.setUuid(UuidUtil.generateUuid());
        entity.setCompanyId(dto.getCompanyId() != null ? dto.getCompanyId() : 1L);
        entity.setUserId(dto.getUserId() != null ? dto.getUserId() : 0L);
        entity.setOperatorName(dto.getOperatorName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : 1);
        entity.setStartTime(dto.getStartTime());
        entity.setStopTime(dto.getStopTime());
        entity.setNote(dto.getNote());
        entity.setCreateBy(dto.getOperateBy());
        entity.setCreateTime(OffsetDateTime.now());
        entity.setIsDel(false);
        return operatorManageMapper.insert(entity) > 0 ? Result.success(true) : Result.fail("新增操作员失败");
    }

    @Override
    public Result<Boolean> update(String uuid, OperatorManageDTO dto) {
        if (!StringUtils.hasText(uuid) || dto == null) return Result.fail("参数错误");
        OperatorManage entity = new OperatorManage();
        entity.setUuid(uuid);
        entity.setCompanyId(dto.getCompanyId());
        entity.setUserId(dto.getUserId());
        entity.setOperatorName(dto.getOperatorName());
        entity.setPhoneNum(dto.getPhoneNum());
        entity.setStatus(dto.getStatus());
        entity.setStartTime(dto.getStartTime());
        entity.setStopTime(dto.getStopTime());
        entity.setNote(dto.getNote());
        entity.setUpdateBy(dto.getOperateBy());
        return operatorManageMapper.updateByUuid(entity) > 0 ? Result.success(true) : Result.fail("更新失败");
    }

    @Override
    public Result<Boolean> deleteByUuid(String uuid, Long updateBy) {
        return operatorManageMapper.deleteByUuid(uuid, updateBy) > 0 ? Result.success(true) : Result.fail("删除失败");
    }

    @Override
    public Result<Boolean> updateStatus(String uuid, Integer status, Long updateBy) {
        return operatorManageMapper.updateStatus(uuid, status, updateBy) > 0
                ? Result.success(true) : Result.fail("更新状态失败");
    }

    @Override
    public Result<Boolean> batchOperation(List<String> uuids, String action, Long updateBy) {
        if (uuids == null || uuids.isEmpty()) {
            return Result.fail("请选择要操作的操作员");
        }
        boolean allSuccess = true;
        for (String uuid : uuids) {
            boolean ok;
            switch (action) {
                case "delete":
                    ok = operatorManageMapper.deleteByUuid(uuid, updateBy) > 0;
                    break;
                case "enable":
                    ok = operatorManageMapper.updateStatus(uuid, 1, updateBy) > 0;
                    break;
                case "disable":
                    ok = operatorManageMapper.updateStatus(uuid, 0, updateBy) > 0;
                    break;
                default:
                    return Result.fail("不支持的操作类型: " + action);
            }
            if (!ok) allSuccess = false;
        }
        return allSuccess ? Result.success(true) : Result.fail("部分操作失败");
    }

    @Override
    public byte[] exportTemplate() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("操作员模板");
            Row headerRow = sheet.createRow(0);
            String[] headers = {"操作员姓名", "手机号码", "状态", "有效期开始", "有效期结束", "备注"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("张三");
            exampleRow.createCell(1).setCellValue("13800138000");
            exampleRow.createCell(2).setCellValue("1");
            exampleRow.createCell(3).setCellValue("2024-01-01 00:00:00");
            exampleRow.createCell(4).setCellValue("2025-12-31 23:59:59");
            exampleRow.createCell(5).setCellValue("示例备注");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            log.error("导出模板失败", e);
            return new byte[0];
        }
    }

    @Override
    public int importFromFile(byte[] fileContent, Long operateBy) {
        if (fileContent == null || fileContent.length == 0) return 0;
        int count = 0;
        try (Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(fileContent))) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    OperatorManageDTO dto = new OperatorManageDTO();
                    dto.setOperatorName(row.getCell(0).getStringCellValue());
                    dto.setPhoneNum(row.getCell(1).getStringCellValue());
                    dto.setStatus((int) row.getCell(2).getNumericCellValue());
                    dto.setOperateBy(operateBy);
                    if (create(dto).getData() == Boolean.TRUE) count++;
                } catch (Exception e) {
                    log.warn("第 {} 行导入失败", i + 1, e);
                }
            }
        } catch (IOException e) {
            log.error("读取文件失败", e);
        }
        return count;
    }

    @Override
    public int importFromSystem(List<OperatorManageDTO> dtoList, Long operateBy) {
        if (dtoList == null || dtoList.isEmpty()) return 0;
        int count = 0;
        for (OperatorManageDTO dto : dtoList) {
            dto.setOperateBy(operateBy);
            if (create(dto).getData() == Boolean.TRUE) count++;
        }
        return count;
    }
}
