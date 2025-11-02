package com.tecnocampus.LS2.protube_back.domain;

import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.user.Role;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TokenClaimsTest {

    @Test
    void validTokenClaims() {
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(3600);
        Set<Role> roles = Set.of( Role.USER);

        TokenClaims tokenClaims = new TokenClaims("user123", now, expires, roles);

        assertEquals("user123", tokenClaims.subject());
        assertEquals(now, tokenClaims.issuedAt());
        assertEquals(expires, tokenClaims.expiresAt());
        assertEquals(roles, tokenClaims.roles());
    }

}