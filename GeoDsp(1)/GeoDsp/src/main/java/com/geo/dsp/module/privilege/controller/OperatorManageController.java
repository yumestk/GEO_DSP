package com.geo.dsp.module.privilege.controller;

import com.geo.dsp.common.result.Result;
import com.geo.dsp.common.vo.PageVo;
import com.geo.dsp.module.privilege.dto.OperatorManageDTO;
import com.geo.dsp.module.privilege.service.OperatorManageService;
import com.geo.dsp.module.privilege.vo.OperatorManageVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/operator")
public class OperatorManageController {

    @Resource
    private OperatorManageService operatorManageService;

    @GetMapping("/list")
    public Result<PageVo<OperatorManageVO>> list(
            @RequestParam(required = false) String operatorName,
            @RequestParam(required = false) String phoneNum,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        OffsetDateTime start = startDate != null ? startDate.atOffset(ZoneOffset.ofHours(8)) : null;
        OffsetDateTime end = endDate != null ? endDate.atOffset(ZoneOffset.ofHours(8)) : null;
        return operatorManageService.pageList(operatorName, status, start, end, pageNum, pageSize);
    }

    @GetMapping("/{uuid}")
    public Result<OperatorManageVO> getDetail(@PathVariable String uuid) {
        return operatorManageService.getByUuid(uuid);
    }

    @PostMapping("/add")
    public Result<Boolean> create(@RequestBody OperatorManageDTO dto,
                                  @RequestParam(defaultValue = "1") Long createBy) {
        dto.setOperateBy(createBy);
        return operatorManageService.create(dto);
    }

    @PutMapping("/update")
    public Result<Boolean> update(@RequestBody OperatorManageDTO dto,
                                  @RequestParam(defaultValue = "1") Long updateBy) {
        dto.setOperateBy(updateBy);
        return operatorManageService.update(dto.getUuid(), dto);
    }

    @DeleteMapping("/{uuid}")
    public Result<Boolean> delete(@PathVariable String uuid, @RequestParam Long updateBy) {
        return operatorManageService.deleteByUuid(uuid, updateBy);
    }

    @PutMapping("/status/{uuid}")
    public Result<Boolean> toggleStatus(@PathVariable String uuid,
                                        @RequestParam Integer status,
                                        @RequestParam Long updateBy) {
        return operatorManageService.updateStatus(uuid, status, updateBy);
    }

    @PostMapping("/batch")
    public Result<Boolean> batchOperation(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> uuids = (List<String>) body.get("uuids");
        String action = (String) body.get("action");
        Object updateByObj = body.get("updateBy");
        Long updateBy = updateByObj != null ? ((Number) updateByObj).longValue() : 1L;
        return operatorManageService.batchOperation(uuids, action, updateBy);
    }

    @GetMapping("/template/download")
    public ResponseEntity<byte[]> downloadTemplate() {
        byte[] template = operatorManageService.exportTemplate();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=operator_template.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(template);
    }

    @PostMapping("/import/file")
    public Result<Integer> importFromFile(@RequestParam("file") MultipartFile file,
                                          @RequestParam Long operateBy) {
        try {
            int count = operatorManageService.importFromFile(file.getBytes(), operateBy);
            return Result.success(count);
        } catch (Exception e) {
            log.error("文件导入失败", e);
            return Result.fail("文件导入失败: " + e.getMessage());
        }
    }

    @PostMapping("/import/system")
    public Result<Integer> importFromSystem(@RequestBody List<OperatorManageDTO> dtoList,
                                            @RequestParam Long operateBy) {
        return Result.success(operatorManageService.importFromSystem(dtoList, operateBy));
    }
}
