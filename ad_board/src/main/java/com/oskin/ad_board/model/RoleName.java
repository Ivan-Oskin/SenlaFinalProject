package com.oskin.ad_board.model;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum RoleName {
    ADMIN("ADMIN"),
    USER("USER");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public SimpleGrantedAuthority toAuthority() {
        return new SimpleGrantedAuthority(("ROLE_") + this.value);
    }
}
