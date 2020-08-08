package com.sunnyz.iiwebapi.base;


import com.sunnyz.iiwebapi.auth.JwtUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


@Configuration
public class UserAuditor implements AuditorAware<String> {
    Logger logger = LoggerFactory.getLogger(UserAuditor.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                return Optional.of("system");
            } else {
                JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
                return Optional.ofNullable(jwtUser.getUserName());
            }

        } catch (Exception ex) {
            logger.error("UserAuditor error: " + ex.getMessage(), ex);
            return Optional.of("system");
        }
    }
}
