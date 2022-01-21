package com.agilework.sims.util;

import lombok.Getter;

public enum ErrorCode {
    NORMAL(0, ""),
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
