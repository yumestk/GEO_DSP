package com.geo.dsp.common.result;

import com.geo.dsp.common.constant.ResultCode;
import org.springframework.modulith.NamedInterface;

import java.io.Serializable;

/**
 * 统一响应结果类
 */
@NamedInterface
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;
    private String msg;    // 响应消息
    private Long total;    // 分页总条数（前端分页需要）

    public Result() {}

    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public Result(int code, String message, T data ,Long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }
    // ========== 成功返回方法 ==========

    /**
     * 成功返回（无参）
     */
/*    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回（枚举）
     */
    public static <T> Result<T> success(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 成功返回（状态码+返回信息）
     */
    public static <T> Result<T> success(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 成功返回（返回信息 + 数据）
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     */
    public static <T> Result<T> success(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 成功返回（数据）
     */
 /*   public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（返回信息）
     */
    public static <T> Result<T> successMsg(String message) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, null);
    }

    // ========== 失败返回方法 ==========

    /**
     * 失败返回（无参）
     */
    public static <T> Result<T> error() {
        return new Result<>(ResultCode.FAIL.getCode(), ResultCode.FAIL.getMessage(), null);
    }

    /**
     * 失败返回（枚举）
     */
    public static <T> Result<T> error(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }

    /**
     * 失败返回（状态码+返回信息）
     */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 失败返回（返回信息+数据）
     */
    public static <T> Result<T> error(String message, T data) {
        return new Result<>(ResultCode.FAIL.getCode(), message, data);
    }

    /**
     * 失败返回（状态码+返回信息+数据）
     */
    public static <T> Result<T> error(int code, String message, T data) {
        return new Result<>(code, message, data);
    }

    /**
     * 失败返回（数据）
     */
    public static <T> Result<T> error(T data) {
        return new Result<>(ResultCode.FAIL.getCode(), ResultCode.FAIL.getMessage(), data);
    }

    /**
     * 失败返回（返回信息）
     */
    public static <T> Result<T> errorMsg(String message) {
        return new Result<>(ResultCode.FAIL.getCode(), message, null);
    }

    // 成功返回（带分页）
    public static <T> Result<T> success(T data, Long total) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        result.setTotal(total);
        return result;
    }

    private void setTotal(Long total) {
    }



    // 成功返回
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMessage("操作成功");
        r.setData(data);
        return r;
    }


    // 失败返回
    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMessage(message);
        return r;
    }
    // Getters and Setters
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    // 前端使用 response.msg，后端使用 message 字段
    public String getMsg() { return message; }
}