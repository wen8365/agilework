package com.agilework.sims.service;

import com.agilework.sims.dto.AdminInfo;
import com.agilework.sims.entity.Admin;
import com.agilework.sims.repository.AdminRepository;
import com.agilework.sims.util.ErrorCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AdminServiceTests {

    @InjectMocks
    private AdminService adminService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private AuthService authService;

    private AdminInfo adminInfo;

    @Before
    public void init() {
        adminInfo = new AdminInfo();
        adminInfo.setTeacherNo("T00abc12345");
        adminInfo.setTeacherName("qwerty");
        adminInfo.setPassword("nRyj545hQ3xdV-");
        adminInfo.setPhone("18600001111");
        adminInfo.setEmail("abc12345@sims.edu.cn");
    }

    @Test
    public void testAdminRegister() {
        Admin admin = new Admin(adminInfo);
        Admin admin1 = new Admin(adminInfo);
        admin1.setId(100);

        Mockito.when(authService.isAdmin(Mockito.any())).thenReturn(true);
        Mockito.when(adminRepository.save(admin)).thenReturn(admin1);

        ErrorCode code = adminService.registerAdmin(adminInfo);
        Assertions.assertEquals(code, ErrorCode.NORMAL);
    }

    @Test
    public void testAdminRegisterNotTeacher() {
        Mockito.when(authService.isAdmin(Mockito.any())).thenReturn(false);

        ErrorCode code = adminService.registerAdmin(adminInfo);
        Assertions.assertEquals(code, ErrorCode.REGISTER_USER_IS_NOT_TEACHER);
    }

    @Test
    public void testQueryAdmin() {
        String tNo = "abc123";
        Mockito.when(adminRepository.findByTeacherNoAndStatus(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(null);

        AdminInfo adminInfo = adminService.queryAdmin(tNo);
        Assertions.assertNull(adminInfo);

        Admin admin = new Admin();
        admin.setTeacherNo(tNo);
        Mockito.when(adminRepository.findByTeacherNoAndStatus(Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(admin);
        adminInfo = adminService.queryAdmin(tNo);
        Assertions.assertNotNull(adminInfo);
        Assertions.assertEquals(adminInfo.getTeacherNo(), tNo);
    }
}
