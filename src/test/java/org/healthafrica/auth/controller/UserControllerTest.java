package org.healthafrica.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.healthafrica.auth.dto.CreateUserRequest;
import org.healthafrica.auth.dto.UserResponse;
import org.healthafrica.auth.entity.Role;
import org.healthafrica.auth.repository.UserRepository;
import org.healthafrica.auth.security.JwtService;
import org.healthafrica.auth.service.UserService;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.healthafrica.tenant.service.TenantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(UserControllerTest.MethodSecurityTestConfig.class)
class UserControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TenantService tenantService;

    @BeforeEach
    void setTenant() {
        TenantContextHolder.setTenant("NGO_A");
    }

    @AfterEach
    void clearTenant() {
        TenantContextHolder.clear();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanListUsers() throws Exception {
        when(userService.listUsers()).thenReturn(List.of(
                new UserResponse(1L, "admin1", "NGO_A", Role.ADMIN, true, Instant.now(), Instant.now())));

        mockMvc.perform(get("/api/users").header("X-Tenant-ID", "NGO_A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin1"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanCreateUser() throws Exception {
        when(userService.createUser(any())).thenReturn(
                new UserResponse(2L, "chw2", "NGO_A", Role.COMMUNITY_HEALTH_WORKER, true, Instant.now(), Instant.now()));

        CreateUserRequest request = new CreateUserRequest("chw2", "password", Role.COMMUNITY_HEALTH_WORKER);

        mockMvc.perform(post("/api/users")
                        .header("X-Tenant-ID", "NGO_A")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("chw2"));
    }

    @Test
    @WithMockUser(roles = "CLINICIAN")
    void nonAdminForbidden() throws Exception {
        mockMvc.perform(get("/api/users").header("X-Tenant-ID", "NGO_A"))
                .andExpect(status().isForbidden());
    }
}
