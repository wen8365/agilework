package com.agilework.sims.controller;

import com.agilework.sims.domain.Session;
import com.agilework.sims.dto.AdminInfo;
import com.agilework.sims.entity.User;
import com.agilework.sims.service.AdminService;
import com.agilework.sims.service.SessionService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.util.UserVerifier;
import com.agilework.sims.vo.AdminQueryResp;
import com.agilework.sims.vo.RegisterReq;
import com.agilework.sims.vo.RegisterResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.text.Normalizer;

import static com.agilework.sims.util.ErrorCode.*;

@RestController
public class AdminController {
    private static final String TAG = "AdminController";

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserVerifier verifier;

    @Autowired
    private SessionService sessionService;

    @Value("${role.teacher}")
    private int roleTeacher;

    @PostMapping("/adminRegister")
    public RegisterResp adminRegister(@RequestBody @NonNull RegisterReq req) {
        String tNo = normalize(req.getTeacherNo());
        String tName = normalize(req.getTeacherName());
        String tPasswd = normalize(req.getPassword());
        String tPhone = normalize(req.getPhone());
        String tEmail = normalize(req.getEmail());

        SLogger.info(TAG, "admin register start, userName=" + tNo);

        // verify username
        if (verifier.isUserNameInvalid(tNo)) {
            SLogger.warn(TAG, REGISTER_USERNAME_FORMAT_ERROR.getReason());
            return new RegisterResp(REGISTER_USERNAME_FORMAT_ERROR);
        }
        // verify teacher name
        if (verifier.isRealNameInvalid(tName)) {
            SLogger.warn(TAG, REGISTER_REALNAME_FORMAT_ERROR.getReason() + ", realName" + tName);
            return new RegisterResp(REGISTER_REALNAME_FORMAT_ERROR);
        }
        // verify password
        if (verifier.isPasswordInvalid(tPasswd)) {
            SLogger.warn(TAG, REGISTER_PASSWORD_LACK_OF_COMPLEXITY.getReason() + ", passwd=" + tPasswd);
            return new RegisterResp(REGISTER_PASSWORD_LACK_OF_COMPLEXITY);
        }
        // verify phone
        if (verifier.isPhoneInvalid(tPhone)) {
            SLogger.warn(TAG, REGISTER_PHONE_FORMAT_ERROR.getReason() + ", phone=" + tPhone);
            return new RegisterResp(REGISTER_PHONE_FORMAT_ERROR);
        }
        // verify email
        if (verifier.isEmailInvalid(tEmail )) {
            SLogger.warn(TAG, REGISTER_EMAIL_FORMAT_ERROR.getReason() + ", email=" + tEmail);
            return new RegisterResp(REGISTER_EMAIL_FORMAT_ERROR);
        }

        AdminInfo adminInfo = new AdminInfo();
        adminInfo.setTeacherNo(tNo);
        adminInfo.setTeacherName(tName);
        adminInfo.setPassword(tPasswd);
        adminInfo.setPhone(tPhone);
        adminInfo.setEmail(tEmail);
        ErrorCode code = adminService.registerAdmin(adminInfo);
        return new RegisterResp(code);
    }

    private String normalize(String str) {
        return Normalizer.normalize(str.replaceAll("[\\p{Cn}]", ""), Normalizer.Form.NFKC);
    }

    @GetMapping("/adminQuery")
    public AdminQueryResp adminQuery(@RequestHeader("sessionId") @NonNull String sessionId) {
        Session session = sessionService.getSession(sessionId);
        SLogger.info(TAG, "admin query start, sessionId=" + sessionId);
        User user = session.getUser();
        if (user.getRole() < roleTeacher) { // deny other role access
            SLogger.warn(TAG, ADMIN_QUERY_PERMISSION_DENIED.getReason()
                    + ", sessionId=" + sessionId);
            return new AdminQueryResp(ADMIN_QUERY_PERMISSION_DENIED);
        }

        AdminQueryResp resp = new AdminQueryResp();
        AdminInfo adminInfo = adminService.queryAdmin(user.getUserNo());
        if (adminInfo != null) {
            resp.setAdminInfo(adminInfo);
        } else {
            resp.setErrCode(ADMIN_QUERY_NOT_EXISTS.getCode());
        }
        return resp;
    }
}
