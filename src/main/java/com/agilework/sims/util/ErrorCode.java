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
    ADMIN_QUERY_PERMISSION_DENIED(1208, "admin query permission denied"),
    ADMIN_QUERY_NOT_EXISTS(1209, "admin not exists"),
    STUDENT_IMPORT_PERMISSION_DENIED(1301, "student import permission denied"),
    STUDENT_IMPORT_USER_ALREADY_EXISTS(1302, "student already exists"),
    STUDENT_QUERY_PERMISSION_DENIED(1303, "student query permission denied"),
    STUDENT_QUERY_NOT_EXISTS(1304, "student not exists"),
    STUDENT_UPDATE_PERMISSION_DENIED(1305, "student update permission denied"),
    STUDENT_UPDATE_INFO_INVALID(1306, "student information invalid"),
    STUDENT_REMOVE_PERMISSION_DENIED(1307, "student remove permission denied"),
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
