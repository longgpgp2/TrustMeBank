package com.trustme.enums;

import java.util.Arrays;
import java.util.Set;

public enum Roles {
    USER("USER", Set.of()),
    ADMIN("ADMIN", Set.of("admin:read", "posts:manage")),
    SUPERADMIN("SUPERADMIN", Set.of("user:manage"));

    private final String role;
    private final Set<String> authorities;

    Roles(String role, Set<String> authorities) {
        this.role = role;
        this.authorities = authorities;
    }

    public String getRole() {
        return role;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public static Roles fromString(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.role.equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName));
    }
}
