package com.nusiss.movie.utils;

import lombok.Getter;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.utils
 * <p>
 * Created by tangyi on 2022-10-21 21:46
 * @author tangyi
 */
@Getter
public enum ResultCodeEnum {
    SUCCESS(200,"Success"),
    FAIL(201, "Fail"),
    DATA_ERROR(204, "Data Error"),
    DATA_UPDATE_ERROR(205, "Data update error"),

    LOGIN_AUTH(208, "Need Login"),
    PERMISSION(209, "No permission"),
    ;

    final private Integer code;
    final private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
