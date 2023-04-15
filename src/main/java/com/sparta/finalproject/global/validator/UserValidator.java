package com.sparta.finalproject.global.validator;

import com.sparta.finalproject.domain.user.entity.User;
import com.sparta.finalproject.global.response.exceptionType.UserException;

import static com.sparta.finalproject.global.enumType.UserRoleEnum.*;
import static com.sparta.finalproject.global.response.CustomStatusCode.UNAUTHORIZED_USER;

public class UserValidator {
    public static void validateTeacherAndPrincipal(User user)  {
        if (user.getRole() != TEACHER && user.getRole() != PRINCIPAL) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateParent(User user)  {
        if (user.getRole() != PARENT) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validatePrincipal(User user) {
        if (user.getRole() != PRINCIPAL) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateEarlyParent(User user) {
        if (user.getRole() != EARLY_PARENT) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateTeacher(User user) {
        if (user.getRole() != EARLY_TEACHER) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateEarlyTeacher(User user) {
        if (user.getRole() != EARLY_TEACHER) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateParentAndTeacherAndPrincipal(User user)  {
        if (user.getRole() != PARENT && user.getRole() != TEACHER && user.getRole() != PRINCIPAL) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateEarlyParentAndEarlyTeacher(User user)  {
        if (user.getRole() != EARLY_PARENT && user.getRole() != EARLY_TEACHER) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateEarlyUser(User user) {
        if (user.getRole() != EARLY_USER) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }

    public static void validateEarlyUserAndEarlyTeacher(User user){
        if (user.getRole() != EARLY_USER && user.getRole() != EARLY_TEACHER) {
            throw new UserException(UNAUTHORIZED_USER);
        }
    }
}
