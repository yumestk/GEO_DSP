package com.geo.dsp.module.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataProject {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 唯一标识
     */
    private String uuid;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 任务ID
     */
    private Long taskId;

    /**
     * 工程名称
     */
    private String engineeringName;

    /**
     * 工作配置
     */
    private String workConfig;

    /**
     * 线号起始值
     */
    private Double linenoStart;

    /**
     * 线号步长
     */
    private Double linenoStep;

    /**
     * 点号起始值
     */
    private Double pointnoStart;

    /**
     * 点号步长
     */
    private Double pointnoStep;

    /**
     * 发射线圈长度
     */
    private Double sendcoilLen;

    /**
     * 发射线圈宽度
     */
    private Double sendcoilWidth;

    /**
     * 发射线圈匝数
     */
    private Integer sendcoilTurns;

    /**
     * 接收线圈尺寸
     */
    private Double recvcoilSize;

    /**
     * 接收线圈增益
     */
    private Double recvcoilGain;

    /**
     * 关断时间
     */
    private Double offtime;

    /**
     * 测点D长度
     */
    private Double pointlenD;

    /**
     * 测点R长度
     */
    private Double pointlenR;

    /**
     * 创建时间（业务字段）
     */
    private LocalDateTime createtime;

    /**
     * 校准编号
     */
    private String calibrateno;

    /**
     * 设备SSID
     */
    private String ssid;

    /**
     * 设备编号
     */
    private String deviceNum;

    /**
     * 天线数量
     */
    private Integer antennaNum;

    /**
     * 操作人ID
     */
    private Long operatorId;

    /**
     * 控制参数
     */
    private String controlParams;

    /**
     * 状态（0-未开始，1-进行中，2-已完成）
     */
    private Integer status;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private OffsetDateTime createTime;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private OffsetDateTime updateTime;

    /**
     * 备注
     */
    private String note;

    /**
     * 是否删除（false-未删，true-已删）
     */
    private Boolean isDel;
}