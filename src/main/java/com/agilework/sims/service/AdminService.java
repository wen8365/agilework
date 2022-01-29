package com.agilework.sims.service;

import com.agilework.sims.dto.AdminInfo;
import com.agilework.sims.entity.Admin;
import com.agilework.sims.repository.AdminRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private static final String TAG = "AdminService";

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AuthService authService;

    @Value("${user-status.normal}")
    private int normalStatus;

    public ErrorCode registerAdmin(AdminInfo adminInfo) {
        SLogger.info(TAG, "register param=" + adminInfo);
        if (!authService.isAdmin(adminInfo)) {
            SLogger.error(TAG, ErrorCode.REGISTER_USER_IS_NOT_TEACHER.getReason());
            return ErrorCode.REGISTER_USER_IS_NOT_TEACHER;
        }

        Admin admin = new Admin(adminInfo);
        Admin result;
        try {
            result = adminRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            SLogger.error(TAG, "register ERROR:" + e.getRootCause());
            return ErrorCode.REGISTER_USER_ALREADY_EXISTS;
        }

        SLogger.info(TAG, "register SUCCESS, result= " + result.getId());
        return ErrorCode.NORMAL;
    }

    public AdminInfo queryAdmin(String tNo) {
        Admin admin =  adminRepository.findByTeacherNoAndStatus(tNo, normalStatus);
        if (admin != null) {
            SLogger.info(TAG, "query SUCCESS, teacherNo= " + admin.getTeacherNo());
            admin.setPassword(""); // don't return password
            return new AdminInfo(admin);
        }
        SLogger.error(TAG, "query FAILED, admin NOT FOUND!");
        return null;
    }
}
