package com.agilework.sims.controller;

import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.vo.LoginReq;
import com.agilework.sims.vo.LoginResp;
import com.agilework.sims.vo.LogoutResp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LoginControllerTests {

    @Autowired
    private LoginController loginController;

    @Test
    public void testLogin() {
        LoginReq req = new LoginReq("MF21320138", "Abc123456=");
        LoginResp resp = loginController.login(req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.NORMAL.getCode());
        Assertions.assertEquals(resp.getRole(), 0);
        Assertions.assertNotNull(resp.getName());
        Assertions.assertNotNull(resp.getSessionId());

        LoginResp resp1 = loginController.isLogin(resp.getSessionId());
        Assertions.assertEquals(resp1.getErrCode(), ErrorCode.NORMAL.getCode());
        Assertions.assertEquals(resp1.getRole(), 0);
        Assertions.assertNull(resp1.getName());
        Assertions.assertNull(resp1.getSessionId());

        LogoutResp resp2 = loginController.logout(resp.getSessionId());
        Assertions.assertEquals(resp2.getErrCode(), ErrorCode.NORMAL.getCode());
    }

    @Test
    public void testUserNameInvalid() {
        LoginReq req = new LoginReq("MF2132", "Abc123456=");
        LoginResp resp = loginController.login(req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.LOGIN_USERNAME_INVALID.getCode());
    }

    @Test
    public void testPasswordError() {
        LoginReq req = new LoginReq("MF21320138", "aaa");
        LoginResp resp = loginController.login(req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.LOGIN_USERNAME_PASSWORD_ERROR.getCode());
    }

    @Test
    public void testUserNotExists() {
        LoginReq req = new LoginReq("MF21320139", "aBc98765!~");
        LoginResp resp = loginController.login(req);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.LOGIN_USERNAME_PASSWORD_ERROR.getCode());
    }

    @Test
    public void testSessionNotExist() {
        String sessionId = "abc";
        LoginResp resp = loginController.isLogin(sessionId);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.LOGIN_NOT_LOGIN.getCode());
        LogoutResp resp1 = loginController.logout(sessionId);
        Assertions.assertEquals(resp1.getErrCode(), ErrorCode.LOGIN_SESSION_ID_ERROR.getCode());

        sessionId = "a1bf710f-2a2f-4a1c-a357-ad8ebe648865";
        resp = loginController.isLogin(sessionId);
        Assertions.assertEquals(resp.getErrCode(), ErrorCode.LOGIN_NOT_LOGIN.getCode());
        resp1 = loginController.logout(sessionId);
        Assertions.assertEquals(resp1.getErrCode(), ErrorCode.LOGIN_SESSION_ID_ERROR.getCode());
    }
}
