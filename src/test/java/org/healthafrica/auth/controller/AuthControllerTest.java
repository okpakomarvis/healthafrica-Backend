package org.healthafrica.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.healthafrica.auth.dto.LoginRequest;
import org.healthafrica.auth.entity.Role;
import org.healthafrica.auth.entity.User;
import org.healthafrica.auth.repository.UserRepository;
import org.healthafrica.auth.security.JwtService;
import org.healthafrica.shared.tenant.TenantContextHolder;
import org.healthafrica.tenant.service.TenantService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller tests for {@link AuthController}.
 */
@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PasswordEncoder passwordEncoder;

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
    void loginReturnsToken() throws Exception {
        User user = User.builder()
                .id(1L)
                .username("chw1")
                .password("hashed")
                .tenantId("NGO_A")
                .role(Role.COMMUNITY_HEALTH_WORKER)
                .enabled(true)
                .build();

        when(userRepository.findByTenantIdAndUsername("NGO_A", "chw1"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.generateToken(1L, "NGO_A", "COMMUNITY_HEALTH_WORKER"))
                .thenReturn("test-jwt-token");

        LoginRequest request = new LoginRequest("chw1", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Tenant-ID", "NGO_A")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"));
    }
}
