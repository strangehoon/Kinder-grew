package com.sparta.finalproject.global.enumType;

import lombok.Getter;

@Getter
public enum CurrentStatus {
    ENTERED("등원"),
    NOT_ENTERED("미등원"),
    EXITED("하원"),
    NOT_EXITED("미하원");
    String currentStatus;

    CurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

}
