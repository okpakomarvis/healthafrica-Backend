package org.healthafrica.auth.dto;

import org.healthafrica.auth.entity.Role;

public record UpdateUserRequest(
        String password,
        Role role,
        Boolean enabled
) {
}
