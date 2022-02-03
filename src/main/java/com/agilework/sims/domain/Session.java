package com.agilework.sims.domain;

import com.agilework.sims.entity.User;
import lombok.Data;

import java.time.Instant;


@Data
public class Session {
    private String sessionId;
    private Instant expireTime;
    private User user;

    public Session(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }
}
