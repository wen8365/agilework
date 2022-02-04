package com.agilework.sims.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class UserVerifier {

    @Value("${user.pattern.username}")
    private String uPattern;

    @Value("${user.pattern.realname}")
    private String rPattern;

    @Value("${user.pattern.password}")
    private String pPattern;

    @Value("${user.pattern.phone}")
    private String phonePattern;

    @Value("${user.pattern.email}")
    private String emailPattern;

    @Value("${user.pattern.sex}")
    private String sexPattern;

    @Value("${user.pattern.major}")
    private String majorPattern;

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

    public boolean isSexInvalid(String sex) {
        return isInvalid(sex, sexPattern);
    }
    public boolean isMajorInvalid(String sex) {
        return isInvalid(sex, majorPattern);
    }

    private boolean isInvalid(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        return !matcher.matches();
    }
}
