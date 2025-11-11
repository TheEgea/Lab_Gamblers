package com.tecnocampus.LS2.protube_back.domain.auth;

public interface TokenService {
    String issue(TokenClaims claims);
}