package org.healthafrica.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.healthafrica.auth.dto.*;
import org.healthafrica.auth.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Users", description = "Tenant user management (ADMIN)")
public class UserController {

    private final UserService userService;

    @GetMapping
    @Operation(summary = "List users", description = "Returns all users in the current tenant.")
    public List<UserResponse> list() {
        return userService.listUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user", description = "Returns a single user by ID within the current tenant.")
    public UserResponse get(
            @Parameter(description = "User ID") @PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user in the current tenant.")
    public UserResponse create(@Valid @RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates user credentials for the given ID.")
    public UserResponse update(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Update user role", description = "Changes the role assigned to a user.")
    public UserResponse updateRole(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UpdateUserRoleRequest request) {
        return userService.updateRole(id, request);
    }

    @PatchMapping("/{id}/enabled")
    @Operation(summary = "Enable or disable user", description = "Toggles whether a user can authenticate.")
    public UserResponse updateEnabled(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody UpdateUserEnabledRequest request) {
        return userService.updateEnabled(id, request);
    }
}
