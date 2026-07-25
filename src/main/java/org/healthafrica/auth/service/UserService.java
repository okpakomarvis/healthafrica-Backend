package org.healthafrica.auth.service;

import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.dto.*;
import org.healthafrica.auth.entity.Role;
import org.healthafrica.auth.entity.User;
import org.healthafrica.auth.repository.UserRepository;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserResponse> listUsers() {
        String tenantId = TenantContextHolder.getTenant();
        return userRepository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUser(Long id) {
        return toResponse(findInTenant(id));
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        String tenantId = TenantContextHolder.getTenant();
        rejectSuperAdminRole(request.role());

        if (userRepository.existsByTenantIdAndUsername(tenantId, request.username())) {
            throw new IllegalArgumentException("Username already exists in this tenant");
        }

        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .tenantId(tenantId)
                .role(request.role())
                .enabled(true)
                .build();

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = findInTenant(id);

        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }

        if (request.role() != null) {
            rejectSuperAdminRole(request.role());
            ensureNotLastAdminDemotion(user, request.role());
            user.setRole(request.role());
        }

        if (request.enabled() != null) {
            if (!request.enabled()) {
                ensureNotLastAdminDisable(user);
            }
            user.setEnabled(request.enabled());
        }

        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateRole(Long id, UpdateUserRoleRequest request) {
        User user = findInTenant(id);
        rejectSuperAdminRole(request.role());
        ensureNotLastAdminDemotion(user, request.role());
        user.setRole(request.role());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateEnabled(Long id, UpdateUserEnabledRequest request) {
        User user = findInTenant(id);
        if (!request.enabled()) {
            ensureNotLastAdminDisable(user);
        }
        user.setEnabled(request.enabled());
        return toResponse(userRepository.save(user));
    }

    private User findInTenant(Long id) {
        return userRepository
                .findByIdAndTenantId(id, TenantContextHolder.getTenant())
                .orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    private void ensureNotLastAdminDisable(User user) {
        if (user.getRole() == Role.ADMIN
                && userRepository.countByTenantIdAndRole(user.getTenantId(), Role.ADMIN) <= 1) {
            throw new IllegalStateException("Cannot disable the last admin in this tenant");
        }
    }

    private void ensureNotLastAdminDemotion(User user, Role newRole) {
        if (user.getRole() == Role.ADMIN
                && newRole != Role.ADMIN
                && userRepository.countByTenantIdAndRole(user.getTenantId(), Role.ADMIN) <= 1) {
            throw new IllegalStateException("Cannot change role of the last admin in this tenant");
        }
    }

    private void rejectSuperAdminRole(Role role) {
        if (role == Role.SUPER_ADMIN) {
            throw new IllegalArgumentException("Cannot assign SUPER_ADMIN role");
        }
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getTenantId(),
                user.getRole(),
                user.isEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
