package com.tecnocampus.LS2.protube_back.domain.auth;

import com.tecnocampus.LS2.protube_back.domain.user.Role;

import java.time.Instant;
import java.util.Set;

public record TokenClaims(String subject, Instant issuedAt, Instant expiresAt, Set<Role> roles) {}