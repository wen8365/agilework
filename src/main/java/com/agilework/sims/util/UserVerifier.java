package com.agilework.sims.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserVerifier {

    @Value("${login.pattern.username}")
    private String uPattern;

    @Value("${login.pattern.realname}")
    private String rPattern;

    @Value("${login.pattern.password}")
    private String pPattern;

    @Value("${login.pattern.phone}")
    private String phonePattern;

    @Value("${login.pattern.email}")
    private String emailPattern;

    public boolean isUserNameInvalid(String userName) {
        return isInvalid(userName, uPattern);
    }

    public boolean isRealNameInvalid(String name) {
        return isInvalid(name, rPattern);
    }

    public boolean isPasswordInvalid(String passwd) {
        return isInvalid(passwd, pPattern);
    }

    public boolean isPhoneInvalid(String phone) {
        return isInvalid(phone, phonePattern);
    }

    public boolean isEmailInvalid(String email) {
        int at = email.indexOf('@');
        if (at < 0) return false;

        String pre = email.substring(0, at);
        String hostname = email.substring(at).toLowerCase(Locale.US);
        String email1 = pre + hostname;
        return isInvalid(email1, emailPattern);
    }

    private boolean isInvalid(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        return !matcher.matches();
    }
}
