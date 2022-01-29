package com.agilework.sims.vo;

import com.agilework.sims.util.ErrorCode;
import lombok.Data;

@Data
public class BaseResp {
    protected int errCode;

    public BaseResp() {
        this.errCode = ErrorCode.NORMAL.getCode();
    }

    public BaseResp(ErrorCode code) {
        this.errCode = code.getCode();
    }
}
