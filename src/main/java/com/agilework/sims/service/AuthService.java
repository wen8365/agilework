package com.agilework.sims.service;

import com.agilework.sims.dto.AdminSummary;

/**
 * Verify the administrator's identity.
 */
public interface AuthService {
    /**
     * Using the admin summary to check whether he is a teacher from third parties.
     * @param summary the summary of an admin user,
     *                contains user number, email or other else necessary.
     * @return true if the admin user is a real teacher,
     *         otherwise the admin will be considered as an attacker.
     */
    boolean isAdmin(AdminSummary summary);
}
