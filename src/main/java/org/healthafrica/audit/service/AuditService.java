package org.healthafrica.audit.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.audit.dto.AuditResponse;
import org.healthafrica.audit.repository.AuditLogRepository;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository repository;

    public List<AuditResponse> findAll() {

        return repository
                .findByTenantId(
                        TenantContextHolder.getTenant()
                )
                .stream()
                .map(log ->
                        new AuditResponse(
                                log.getId(),
                                log.getUserId(),
                                log.getTenantId(),
                                log.getAction(),
                                log.getTimestamp()
                        )
                )
                .toList();
    }
}