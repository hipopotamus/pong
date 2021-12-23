package com.hipo.domain.entity.enums;

import lombok.Getter;

@Getter
public enum Role {
    User("ROLE_USER");

    private String roleName;

    Role(String role) {
        this.roleName = role;
    }
}
