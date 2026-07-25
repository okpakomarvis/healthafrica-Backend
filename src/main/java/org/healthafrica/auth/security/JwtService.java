package org.healthafrica.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

/**
 * Issues and validates JWT access tokens for authenticated API requests.
 */
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(Long userId, String tenantId, String role) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .claim("userId", userId)
                .claim("tenantId", tenantId)
                .claim("role", role)
                .subject(userId.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    public Optional<JwtClaims> parseToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Optional.of(new JwtClaims(
                    toLong(claims.get("userId")),
                    claims.get("tenantId", String.class),
                    claims.get("role", String.class)
            ));
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

    private Long toLong(Object value) {
        return switch (value) {
            case null -> throw new JwtException("Missing userId claim");
            case Long longValue -> longValue;
            case Integer intValue -> intValue.longValue();
            case String stringValue -> Long.parseLong(stringValue);
            default -> throw new JwtException("Invalid userId claim type");
        };
    }

    /**
     * Authenticated principal claims extracted from a JWT.
     */
    public record JwtClaims(Long userId, String tenantId, String role) {
    }
}
