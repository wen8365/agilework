package com.agilework.sims.service;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.User;
import com.agilework.sims.repository.UserRepository;
import com.agilework.sims.util.ErrorCode;
import com.agilework.sims.util.SLogger;
import com.agilework.sims.vo.LoginResp;
import com.agilework.sims.vo.LogoutResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private static final String TAG = "LoginService";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionService sessionService;

    public LoginResp login(String userNo, String password) {
        User user = userRepository.findByUserNoAndPassword(userNo, password);
        if (user != null) {
            user.setPassword(""); // no need password
            String sessionId = sessionService.createSession(user);
            String log = "user login SUCCESS, userNo=%s, userName=%s, role=%d, sessionId=%s";
            SLogger.info(TAG, String.format(log,
                    userNo, user.getUserName(), user.getRole(), sessionId));
            return new LoginResp(user.getRole(), user.getUserName(), sessionId);
        }
        SLogger.error(TAG, "user login FAILED, userNo=" + userNo);
        return LoginResp.failureResp(ErrorCode.LOGIN_USERNAME_PASSWORD_ERROR);
    }

    public LoginResp isLogin(String sessionId) {
        Session session = sessionService.getSession(sessionId);
        if (session == null) {
            SLogger.info(TAG, "session NOT FOUND, id=" + sessionId);
            return LoginResp.failureResp(ErrorCode.LOGIN_NOT_LOGIN);
        }
        User user = session.getUser();
        LoginResp resp = new LoginResp();
        resp.setErrCode(ErrorCode.NORMAL.getCode());
        resp.setRole(user.getRole());

        SLogger.info(TAG, "user has logged in, userNo=" + user.getUserNo()
                + ", sessionId=" + sessionId);
        return resp;
    }

    public LogoutResp logout(String sessionId) {
        boolean isDestroyed = sessionService.destroySession(sessionId);
        if (!isDestroyed) {
            SLogger.error(TAG, "user logout FAILED, sessionId=" + sessionId);
            return new LogoutResp(ErrorCode.LOGIN_SESSION_ID_ERROR);
        }
        SLogger.info(TAG, "user logout SUCCESS, sessionId=" + sessionId);
        return new LogoutResp();
    }
}
