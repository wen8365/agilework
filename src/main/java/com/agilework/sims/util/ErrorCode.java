package com.agilework.sims.util;

import lombok.Getter;

public enum ErrorCode {
    NORMAL(0, ""),
    LOGIN_NOT_LOGIN(1101, "user not login"),
    LOGIN_USERNAME_INVALID(1102, "username invalid"),
    LOGIN_USERNAME_PASSWORD_ERROR(1103, "username or password error"),
    LOGIN_SESSION_ID_ERROR(1104, "sessionId error"),
    REGISTER_USERNAME_FORMAT_ERROR(1201, "username format error"),
    REGISTER_REALNAME_FORMAT_ERROR(1202, "real name format error"),
    REGISTER_PASSWORD_LACK_OF_COMPLEXITY(1203, "password lack of complexity"),
    REGISTER_PHONE_FORMAT_ERROR(1204, "phone format error"),
    REGISTER_EMAIL_FORMAT_ERROR(1205, "email format error"),
    REGISTER_USER_ALREADY_EXISTS(1206, "user already exists"),
    REGISTER_USER_IS_NOT_TEACHER(1207, "user isn't a teacher"),
    ADMIN_QUERY_PERMISSION_DENIED(1208, "user permission denied"),
    ADMIN_QUERY_NOT_EXISTS(1209, "admin not exists"),
    UNKNOWN(1999, "unknown error");

    @Getter
    private final int code;
    @Getter
    private final String reason;

    ErrorCode(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }
}
