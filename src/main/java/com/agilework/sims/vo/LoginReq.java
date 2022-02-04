package com.agilework.sims.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginReq {
    @NonNull
    public String userName;
    @NonNull
    public String password;
}
