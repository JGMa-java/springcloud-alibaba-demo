package com.txlc.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: JGMa
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseRes<T> implements Serializable {

    private T data;
    private Integer status;
    private String msg;

    public static <T> BaseRes<T> ok(String msg) {
        return of(null, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> BaseRes<T> ok(T model, String msg) {
        return of(model, CodeEnum.SUCCESS.getCode(), msg);
    }

    public static <T> BaseRes<T> ok(T model) {
        return of(model, CodeEnum.SUCCESS.getCode(), "");
    }

    public static <T> BaseRes<T> of(T datas, Integer code, String msg) {
        return new BaseRes<>(datas, code, msg);
    }

    public static <T> BaseRes<T> err(String msg) {
        return of(null, CodeEnum.ERROR.getCode(), msg);
    }

    public static <T> BaseRes<T> err(T model, String msg) {
        return of(model, CodeEnum.ERROR.getCode(), msg);
    }
}
