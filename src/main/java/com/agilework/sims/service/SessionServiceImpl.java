package com.agilework.sims.service;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.User;
import com.agilework.sims.util.SLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class SessionServiceImpl implements SessionService {
    private static final String TAG = "SessionService";
    private final Map<String, Session> sessionMap = new HashMap<>();

    @Value("${login.session.expire}")
    private int sessionExpire;

    public String createSession(User user) {
        UUID uuid = UUID.randomUUID();
        Session session = new Session(uuid.toString(), user);
        Instant instant = Instant.now();
        instant = instant.plus(Duration.ofMinutes(sessionExpire));
        session.setExpireTime(instant);
        sessionMap.put(uuid.toString(), session);

        SLogger.info(TAG, "store session, sessionId=" + session.getSessionId());
        return uuid.toString();
    }

    public Session getSession(String sessionId) {
        if (isSessionInvalid(sessionId)) {
            return null;
        }
        return sessionMap.get(sessionId);
    }

    public boolean updateSessionExpireTime(String sessionId) {
        if (isSessionInvalid(sessionId)) {
            return false;
        }
        Session session = sessionMap.get(sessionId);
        if (session == null) {
            SLogger.error(TAG, "update FAILED, session NOT FOUND, id=" + sessionId);
            return false;
        }
        Instant oldExpire = session.getExpireTime();
        Instant instant = Instant.now();
        instant = instant.plus(Duration.ofMinutes(sessionExpire));
        session.setExpireTime(instant);

        SLogger.info(TAG, "update session expire time, old=" + oldExpire + ", new=" + instant);
        return true;
    }

    public boolean destroySession(String sessionId) {
        if (isSessionInvalid(sessionId)) {
            return false;
        }
        Session session = sessionMap.remove(sessionId);
        if (session == null) {
            SLogger.error(TAG, "destroy FAILED, session NOT FOUND, id=" + sessionId);
            return false;
        }
        String userNo = session.getUser().getUserNo();
        String id = session.getSessionId();
        SLogger.info(TAG, "destroy session, userNo=" + userNo + ", sessionId=" + id);
        return true;
    }

    private boolean isSessionInvalid(String sessionId) {
        try {
            UUID uuid = UUID.fromString(sessionId);
            SLogger.info(TAG, "session id verification passed, id=" + uuid);
        } catch (IllegalArgumentException iae) {
            SLogger.error(TAG, "invalid session, id=" + sessionId);
            return true;
        }
        return false;
    }

    @Scheduled(cron = "0 */30 * * * ?")
    private void autoDestroyExpiredSession() {
        SLogger.info(TAG, "auto destroy expired session");
        Collection<Session> sessions = sessionMap.values();
        Iterator<Session> iterator = sessions.iterator();
        Instant now = Instant.now();
        while (iterator.hasNext()) {
            Session session = iterator.next();
            if (now.isAfter(session.getExpireTime())) {
                iterator.remove();
                SLogger.info(TAG, "session has been destroy, id=" + session.getSessionId());
            }
        }
    }
}
