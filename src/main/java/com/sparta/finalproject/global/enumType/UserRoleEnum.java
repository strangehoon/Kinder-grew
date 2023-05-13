package com.sparta.finalproject.global.enumType;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    EARLY_USER(Authority.EARLY_USER),
    EARLY_TEACHER(Authority.EARLY_TEACHER),
    EARLY_PARENT(Authority.EARLY_PARENT),
    PARENT(Authority.PARENT),
    TEACHER(Authority.TEACHER),
    PRINCIPAL(Authority.PRINCIPAL),
    EARLY_PRINCIPAL(Authority.EARLY_PRINCIPAL);

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {

        public static final String EARLY_USER = "ROLE_EARLY_USER";
        public static final String EARLY_TEACHER = "ROLE_EARLY_TEACHER";
        public static final String EARLY_PARENT = "ROLE_EARLY_PARENT";
        public static final String PARENT = "ROLE_PARENT";
        public static final String TEACHER = "ROLE_TEACHER";
        public static final String PRINCIPAL = "ROLE_PRINCIPAL";
        public static final String EARLY_PRINCIPAL = "ROLE_EARLY_PRINCIPAL";
    }
}
