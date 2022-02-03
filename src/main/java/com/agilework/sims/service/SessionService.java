package com.agilework.sims.service;

import com.agilework.sims.domain.Session;
import com.agilework.sims.entity.User;

public interface SessionService {
    /**
     * Create a new session when user log in.
     * @param user user that try to log in
     * @return session id
     */
    String createSession(User user);

    /**
     * Get a session by the given id
     * @param sessionId the session id
     * @return the session
     */
    Session getSession(String sessionId);

    /**
     * Update a session's timestamp by the given id
     * in order to avoid destroy by system automatically
     * when it has been idle for too long.
     * @param sessionId the session id
     * @return true if updated, false otherwise
     */
    boolean updateSessionExpireTime(String sessionId);

    /**
     * Destroy session by the given id
     * @param sessionId the session id
     * @return true if destroyed, false otherwise
     */
    boolean destroySession(String sessionId);
}
