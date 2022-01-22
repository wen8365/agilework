package com.agilework.sims.vo;

import com.agilework.sims.util.ErrorCode;
import lombok.Data;

@Data
public class LogoutResp {
    private int errCode;

    public LogoutResp() {
        this.errCode = 0;
    }

    public LogoutResp(ErrorCode errCode) {
        this.errCode = errCode.getCode();
    }
}
