package com.agilework.sims.controller;

import com.agilework.sims.service.LoginService;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.vo.LoginReq;
import com.agilework.sims.vo.LoginResp;
import com.agilework.sims.vo.LogoutResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class LoginController {
    private final static String TAG = "LoginController";

    @Value("${login.pattern.username}")
    private String uPattern;

    @Value("${login.pattern.password}")
    private String pPattern;

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public LoginResp login(@RequestBody @NonNull LoginReq req) {
        String userName = req.getUserName();
        String password = req.getPassword();
        String uName = Normalizer.normalize(userName.replaceAll("[\\p{Cn}]", ""), Normalizer.Form.NFKC);
        String passwd = Normalizer.normalize(password.replaceAll("[\\p{Cn}]", ""), Normalizer.Form.NFKC);
        // verify username
        if (isInvalid(uName, uPattern)) {
            SLogger.warn(TAG, "invalid user attempt login, userName=" + uName);
            return LoginResp.failureResp(ErrorCode.LOGIN_USERNAME_INVALID);
        }
        // verify password
        if (isInvalid(passwd, pPattern)) {
            return LoginResp.failureResp(ErrorCode.LOGIN_USERNAME_PASSWORD_ERROR);
        }
        SLogger.info(TAG, "user login start, userName=" + uName);
        return loginService.login(uName, passwd);
    }

    @PostMapping("/logout")
    public LogoutResp logout(String sessionId) {
        String id = Normalizer.normalize(sessionId.replaceAll("[\\p{Cn}]", ""), Normalizer.Form.NFKC);
        SLogger.info(TAG, "user logout start, sessionId=" + id);
        return loginService.logout(id);
    }

    @GetMapping("/isLogin")
    public LoginResp isLogin(String sessionId) {
        String id = Normalizer.normalize(sessionId.replaceAll("[\\p{Cn}]", ""), Normalizer.Form.NFKC);
        SLogger.info(TAG, "check login, sessionId=" + id);
        return loginService.isLogin(id);
    }

    private boolean isInvalid(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        return !matcher.matches();
    }
}
