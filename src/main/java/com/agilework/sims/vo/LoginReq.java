package com.agilework.sims.vo;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class LoginReq {
    @NonNull
    public String userName;
    @NonNull
    public String password;
}
