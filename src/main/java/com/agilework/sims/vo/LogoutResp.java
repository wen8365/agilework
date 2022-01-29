package com.agilework.sims.vo;

import com.agilework.sims.util.ErrorCode;

public class LogoutResp extends BaseResp {

    public LogoutResp() {
        super();
    }

    public LogoutResp(ErrorCode errCode) {
        super(errCode);
    }
}
