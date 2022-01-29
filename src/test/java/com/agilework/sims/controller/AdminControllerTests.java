package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.AdminInfo;
import com.agilework.sims.entity.User;
import com.agilework.sims.service.AdminService;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.UserVerifier;
import com.agilework.sims.vo.AdminQueryResp;
import com.agilework.sims.vo.RegisterReq;
import com.agilework.sims.vo.RegisterResp;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static com.agilework.sims.util.ErrorCode.*;

@RunWith(MockitoJUnitRunner.class)
public class AdminControllerTests {

    @InjectMocks
    private AdminController adminController;
    @Mock
    private AdminService adminService;
    @Mock
    private UserVerifier verifier;
    @Mock
    private SessionService sessionService;
    private RegisterReq req;

    @Before
    public void init() {
        req = new RegisterReq();
        req.setTeacherNo("t0012345");
        req.setTeacherName("Jerry<>");
        req.setPassword("1qazXsw2");
        req.setPhone("11912344321");
        req.setEmail("jerry@sims.edu.cn");

        ReflectionTestUtils.setField(adminController, "roleTeacher", 1);
    }

    @Test
    public void testAdminRegister() {
        Mockito.when(adminService.registerAdmin(Mockito.any()))
                .thenReturn(NORMAL);
        RegisterResp resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), NORMAL.getCode());

        Mockito.when(adminService.registerAdmin(Mockito.any()))
                .thenReturn(REGISTER_USER_IS_NOT_TEACHER);
        resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_USER_IS_NOT_TEACHER.getCode());

        Mockito.when(adminService.registerAdmin(Mockito.any()))
                .thenReturn(REGISTER_USER_ALREADY_EXISTS);
        resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_USER_ALREADY_EXISTS.getCode());
    }

    @Test
    public void testAdminRegisterInfoInvalid() {
        Mockito.when(verifier.isEmailInvalid(Mockito.anyString())).thenReturn(true);
        RegisterResp resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_EMAIL_FORMAT_ERROR.getCode());

        Mockito.when(verifier.isPhoneInvalid(Mockito.anyString())).thenReturn(true);
        resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_PHONE_FORMAT_ERROR.getCode());

        Mockito.when(verifier.isPasswordInvalid(Mockito.anyString())).thenReturn(true);
        resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_PASSWORD_LACK_OF_COMPLEXITY.getCode());

        Mockito.when(verifier.isRealNameInvalid(Mockito.anyString())).thenReturn(true);
        resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_REALNAME_FORMAT_ERROR.getCode());

        Mockito.when(verifier.isUserNameInvalid(Mockito.anyString())).thenReturn(true);
        resp = adminController.adminRegister(req);
        Assertions.assertEquals(resp.getErrCode(), REGISTER_USERNAME_FORMAT_ERROR.getCode());
    }

    @Test
    public void testAdminQuery() {
        User user = new User();
        user.setUserNo("test");
        user.setRole(0); // role student
        String sessionId = "3b46db28-6c6c-4098-9dfc-e5061b152d42";
        Session session = new Session(sessionId, user);

        Mockito.when(sessionService.getSession(sessionId)).thenReturn(session);
        AdminQueryResp resp = adminController.adminQuery(sessionId);
        Assertions.assertEquals(resp.getErrCode(), ADMIN_QUERY_PERMISSION_DENIED.getCode());

        user.setRole(1); // role teacher
        session.setUser(user);
        Mockito.when(adminService.queryAdmin(Mockito.anyString())).thenReturn(null);
        resp = adminController.adminQuery(sessionId);
        Assertions.assertEquals(resp.getErrCode(), ADMIN_QUERY_NOT_EXISTS.getCode());

        Mockito.when(adminService.queryAdmin(Mockito.anyString())).thenReturn(new AdminInfo());
        resp = adminController.adminQuery(sessionId);
        Assertions.assertEquals(resp.getErrCode(), NORMAL.getCode());
    }
}
