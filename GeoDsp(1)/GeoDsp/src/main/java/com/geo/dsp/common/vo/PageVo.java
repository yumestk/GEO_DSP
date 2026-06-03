package com.geo.dsp.common.vo;

import lombok.Data;
import java.util.List;

/**
 * 通用分页查询参数
 * 与前端分页组件对接，currentPage-当前页，pageSize-每页条数
 */
@Data
public class PageVo<T> {
    private List<T> records;   // 数据列表
    private long total;         // 总条数
    private int pageNum;        // 当前页码
    private int pageSize;       // 每页条数
}