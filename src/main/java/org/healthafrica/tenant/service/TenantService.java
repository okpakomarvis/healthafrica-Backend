package org.healthafrica.tenant.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.platform.PlatformConstants;
import org.healthafrica.tenant.dto.*;
import org.healthafrica.tenant.entity.Tenant;
import org.healthafrica.tenant.repository.TenantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository repository;

    public List<PublicTenantResponse> listEnabledForLogin() {
        return repository.findByEnabledTrueOrderByNameAsc().stream()
                .filter(t -> !PlatformConstants.PLATFORM_TENANT.equals(t.getCode()))
                .map(t -> new PublicTenantResponse(t.getCode(), t.getName()))
                .toList();
    }

    public List<TenantResponse> listAll() {
        return repository.findAllByOrderByNameAsc().stream()
                .filter(t -> !PlatformConstants.PLATFORM_TENANT.equals(t.getCode()))
                .map(this::toResponse)
                .toList();
    }

    public boolean isEnabled(String code) {
        return repository.findByCode(code)
                .map(Tenant::isEnabled)
                .orElse(false);
    }

    public void assertEnabledForLogin(String code) {
        if (PlatformConstants.PLATFORM_TENANT.equals(code)) {
            return;
        }
        Tenant tenant = repository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown tenant"));
        if (!tenant.isEnabled()) {
            throw new IllegalArgumentException("Tenant is disabled");
        }
    }

    @Transactional
    public TenantResponse create(CreateTenantRequest request) {
        if (PlatformConstants.PLATFORM_TENANT.equals(request.code())) {
            throw new IllegalArgumentException("Reserved tenant code");
        }
        if (repository.existsByCode(request.code())) {
            throw new IllegalArgumentException("Tenant code already exists");
        }

        Tenant tenant = new Tenant();
        tenant.setCode(request.code());
        tenant.setName(request.name());
        tenant.setEnabled(true);
        return toResponse(repository.save(tenant));
    }

    @Transactional
    public TenantResponse updateEnabled(String code, UpdateTenantEnabledRequest request) {
        if (PlatformConstants.PLATFORM_TENANT.equals(code)) {
            throw new IllegalArgumentException("Cannot disable platform tenant");
        }

        Tenant tenant = repository.findByCode(code)
                .orElseThrow(() -> new NoSuchElementException("Tenant not found"));
        tenant.setEnabled(request.enabled());
        return toResponse(repository.save(tenant));
    }

    private TenantResponse toResponse(Tenant tenant) {
        return new TenantResponse(
                tenant.getId(),
                tenant.getCode(),
                tenant.getName(),
                tenant.isEnabled(),
                tenant.getSubscriptionStatus(),
                tenant.getCreatedAt(),
                tenant.getUpdatedAt());
    }
}
