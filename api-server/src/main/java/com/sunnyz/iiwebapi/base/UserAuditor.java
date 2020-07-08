package com.sunnyz.iiwebapi.base;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;


@Configuration
public class UserAuditor implements AuditorAware<String> {
    Logger logger = LoggerFactory.getLogger(UserAuditor.class);

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return Optional.of("system");
        } catch (Exception ex) {
            logger.error("UserAuditor error: " + ex.getMessage(), ex);
            return Optional.of("system");
        }
    }
}
