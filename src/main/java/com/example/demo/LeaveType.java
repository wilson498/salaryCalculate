package com.example.demo;

import lombok.Getter;

public enum LeaveType {
    SPECIAL(0),
    SICK(0.5),
    PERSONAL(1),
    ;
    @Getter
    private final double leaveDayNum;

    LeaveType(double leaveDayNum) {
        this.leaveDayNum = leaveDayNum;
    }
}
