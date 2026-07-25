package org.healthafrica.shared.security;

import jakarta.servlet.FilterChain;
import org.healthafrica.auth.security.AuthenticatedUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class PlatformAccessFilterTest {

    private final PlatformAccessFilter filter = new PlatformAccessFilter();

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void allowsSuperAdminPlatformApi() throws Exception {
        setSuperAdmin();
        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/platform/tenants");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void blocksSuperAdminFromTenantOperationalApi() {
        setSuperAdmin();
        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/gis/vaccination-map");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        assertThrows(
                AccessDeniedException.class,
                () -> filter.doFilter(request, response, chain));
    }

    @Test
    void skipsFilterForTenantAdmin() throws Exception {
        setTenantAdmin();
        MockHttpServletRequest request =
                new MockHttpServletRequest("GET", "/api/gis/vaccination-map");
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        assertDoesNotThrow(() -> filter.doFilter(request, response, chain));
        verify(chain).doFilter(request, response);
    }

    private void setSuperAdmin() {
        var auth = new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser(1L, "PLATFORM", "SUPER_ADMIN"),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private void setTenantAdmin() {
        var auth = new UsernamePasswordAuthenticationToken(
                new AuthenticatedUser(2L, "NGO_A", "ADMIN"),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
