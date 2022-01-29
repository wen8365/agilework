package com.agilework.sims.vo;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class RegisterReq {
    @NonNull
    private String teacherNo;
    @NonNull
    private String teacherName;
    @NonNull
    private String password;
    @NonNull
    private String phone;
    @NonNull
    private String email;

    public RegisterReq() {}
}
