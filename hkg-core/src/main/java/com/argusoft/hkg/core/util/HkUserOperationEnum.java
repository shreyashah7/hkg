package com.argusoft.hkg.core.util;

public enum HkUserOperationEnum {

    PUNCH_IN(1), PUNCH_OUT(2), LOGIN(3), LOGOUT(4);

    private final Integer value;

    private HkUserOperationEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return value;
    }
}
