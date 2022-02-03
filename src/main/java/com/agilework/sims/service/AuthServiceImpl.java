package com.agilework.sims.service;

import com.agilework.sims.dto.AdminSummary;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public boolean isAdmin(AdminSummary summary) {
        // Assume that there is a teacher information management system,
        // and supports check teacher's identity via teacher number or email.
        String tNo = summary.getTeacherNo();
        String email = summary.getEmail().toLowerCase(Locale.US);
        return tNo.startsWith("T00") && email.endsWith("@sims.edu.cn");
    }
}
