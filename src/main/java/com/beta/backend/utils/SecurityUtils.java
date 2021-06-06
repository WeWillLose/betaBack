package com.beta.backend.utils;

import com.beta.backend.env.ROLES;
import com.beta.backend.model.EReportStatus;
import com.beta.backend.model.Report;
import com.beta.backend.model.Role;
import com.beta.backend.model.User;
import com.beta.backend.service.user.UserService;
import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserService userService;

    private SecurityUtils(@Lazy UserService userService) {
        this.userService = userService;
    }


    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null;
    }

    /**
     * Checks if the current user has a specific role.
     *
     * @param role the role to check.
     * @return true if the current user has the role, false otherwise.
     */
    public static boolean hasCurrentUserThisRole(Role role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.getAuthorities().contains(role);
    }

    public static boolean isAdmin(User user) {
        return user != null && user.getRoles().contains(ROLES.ADMIN);
    }

    public static boolean isChairman(User user) {
        return user != null && user.getRoles().contains(ROLES.CHAIRMAN);
    }

    public static boolean isCurrentUserAdmin() {
        return hasCurrentUserThisRole(ROLES.ADMIN);
    }

    public static boolean isCurrentUserChairman() {
        return hasCurrentUserThisRole(ROLES.CHAIRMAN);
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication != null && !authentication.getPrincipal().equals("anonymousUser") ? (User) authentication.getPrincipal() : null;
    }

    public boolean isCurrentUserCanEditReportIfIsOwnerAndReportStatusUNCHECKEDOrIsOwnerChairmanOrAdmin(@NonNull Report report) {
        if (isCurrentUserAdmin()) return true;
        if (report.getAuthor() == null) return false;
        if (getCurrentUser() == null) return false;
        if (isCurrentUserChairman() &&
                userService.isUserInChairmanGroup(report.getAuthor().getId(), getCurrentUser().getId())) {
            return true;
        }
        return report.getStatus() == EReportStatus.UNCHECKED && report.getAuthor().getId().equals(SecurityUtils.getCurrentUser().getId());
    }

    public static boolean isCurrentUserEqualsUserOrAdmin(User user) {
        if (hasCurrentUserThisRole(ROLES.ADMIN)) return true;
        if (user == null) return false;
        User currUser = getCurrentUser();
        return currUser != null && currUser.getId().equals(user.getId());
    }
}
