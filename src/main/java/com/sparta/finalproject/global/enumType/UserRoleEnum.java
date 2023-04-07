package com.sparta.finalproject.global.enumType;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    EARLY_USER(Authority.EARLY_USER),
    USER(Authority.USER),
    ADMIN(Authority.ADMIN);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {

        public static final String EARLY_USER = "ROLE_EARLY_USER";
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
