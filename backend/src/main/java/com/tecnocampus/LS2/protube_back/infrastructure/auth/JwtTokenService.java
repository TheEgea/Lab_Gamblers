package com.tecnocampus.LS2.protube_back.infrastructure.auth;

import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.user.Role;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtTokenService implements TokenService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    public JwtTokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public String issue(TokenClaims claims) {
        Instant now = Instant.now();
        Instant expiry = now.plus(24, ChronoUnit.HOURS);

        JwtClaimsSet jwtClaims = JwtClaimsSet.builder()
                .subject(claims.subject())
                .issuedAt(now)
                .expiresAt(expiry)
                .claim("roles", claims.roles().stream()
                        .map(Role::name)
                        .collect(Collectors.toList()))
                .build();

        JwsHeader header = JwsHeader.with(() -> "HS512").build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, jwtClaims)).getTokenValue();
    }

    @Override
    public TokenClaims verify(String token) {
        Jwt jwt = jwtDecoder.decode(token);
        
        @SuppressWarnings("unchecked")
        Set<Role> roles = ((java.util.List<String>) jwt.getClaim("roles")).stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());

        return new TokenClaims(
                jwt.getSubject(),
                jwt.getIssuedAt(),
                jwt.getExpiresAt(),
                roles
        );
    }
}
