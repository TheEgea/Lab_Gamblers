package com.tecnocampus.LS2.protube_back.security.jwt;

import com.tecnocampus.LS2.protube_back.domain.auth.TokenClaims;
import com.tecnocampus.LS2.protube_back.domain.auth.TokenService;
import com.tecnocampus.LS2.protube_back.domain.user.Role;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtTokenService implements TokenService {

    private final JwtEncoder encoder;
    private final JwtDecoder decoder;

    public JwtTokenService(JwtEncoder encoder, JwtDecoder decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    @Override
    public String issue(TokenClaims claims) {
        JwtClaimsSet set = JwtClaimsSet.builder()
                .subject(claims.subject())
                .issuedAt(claims.issuedAt())
                .expiresAt(claims.expiresAt())
                .claim("roles", claims.roles().stream().map(Enum::name).toArray(String[]::new))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, set)).getTokenValue();
    }


    public String getUsernameFromToken(String token) {
        Jwt jwt = decoder.decode(token);
        return jwt.getSubject();
    }
}
