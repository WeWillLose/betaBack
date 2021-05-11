package com.beta.backend.security;

import com.beta.backend.utils.SecurityUtils;
import com.beta.backend.utils.UserUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuditorAwareImpl  implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(UserUtils.getShortFioFromUser(SecurityUtils.getCurrentUser()));
    }
}
