package org.healthafrica.auth.security;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link JwtService}.
 */
class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void generatesAndParsesToken() {
        ReflectionTestUtils.setField(
                jwtService, "secret",
                "my-super-secret-key-change-in-production-must-be-long-enough");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);

        String token = jwtService.generateToken(1L, "NGO_A", "ADMIN");

        Optional<JwtService.JwtClaims> claims = jwtService.parseToken(token);

        assertTrue(claims.isPresent());
        assertEquals(1L, claims.get().userId());
        assertEquals("NGO_A", claims.get().tenantId());
        assertEquals("ADMIN", claims.get().role());
    }

    @Test
    void rejectsInvalidToken() {
        ReflectionTestUtils.setField(
                jwtService, "secret",
                "my-super-secret-key-change-in-production-must-be-long-enough");
        ReflectionTestUtils.setField(jwtService, "expiration", 86400000L);

        assertTrue(jwtService.parseToken("invalid.token.value").isEmpty());
    }
}
