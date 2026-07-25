package org.healthafrica.auth.repository;

import org.healthafrica.auth.entity.Role;
import org.healthafrica.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByTenantIdAndUsername(String tenantId, String username);

    Optional<User> findByIdAndTenantId(Long id, String tenantId);

    List<User> findByTenantId(String tenantId);

    boolean existsByTenantIdAndUsername(String tenantId, String username);

    long countByTenantIdAndRole(String tenantId, Role role);
}
