package com.agilework.sims.vo;

import com.agilework.sims.util.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 登录响应
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResp extends BaseResp {
    /**
     * 前端用来确定登录的用户的身份<br/>
     * -1 - 无效身份<br/>
     * 0 - 学生<br/>
     * 1 - 老师
     */
    private int role = -1;
    private String name;
    private String sessionId;

    public LoginResp() {
        super();
    }

    public LoginResp(int role, String name, String sessionId) {
        this.role = role;
        this.name = name;
        this.errCode = ErrorCode.NORMAL.getCode();
        this.sessionId = sessionId;
    }

    public static LoginResp failureResp(ErrorCode errorCode) {
        LoginResp resp = new LoginResp();
        resp.setErrCode(errorCode.getCode());
        return resp;
    }
}
