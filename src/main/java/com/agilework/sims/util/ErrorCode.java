package com.agilework.sims.util;

import lombok.Getter;

public enum ErrorCode {
    NORMAL(0, ""),
    LOGIN_NOT_LOGIN(1101, "user not login"),
    LOGIN_USERNAME_INVALID(1102, "username invalid"),
    LOGIN_USERNAME_PASSWORD_ERROR(1103, "username or password error"),
    LOGIN_SESSION_ID_ERROR(1104, "sessionId error"),
    UNKNOWN(1999, "unknown error");

    @Getter
    private int code;
    @Getter
    private String reason;

    ErrorCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}
