package com.rj.Enotes_API_Service.config;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;

public class AuditAwareConfig implements AuditorAware<Integer>{

    @Override
    public Optional<Integer> getCurrentAuditor() {
        return Optional.of(1);
    }

}
