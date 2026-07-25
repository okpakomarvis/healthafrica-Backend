package org.healthafrica.auth.dto;

import jakarta.validation.constraints.NotNull;
import org.healthafrica.auth.entity.Role;

public record UpdateUserRoleRequest(@NotNull Role role) {
}
