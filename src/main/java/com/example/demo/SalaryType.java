package com.example.demo;

import lombok.Getter;

public enum SalaryType {
    HOURLY((double) 1 / 8),

    DAILY(1),

    WEEKLY(7),

    FORTNIGHTLY(14),

    MONTHLY(-1);

    @Getter
    private final double workDays;

    SalaryType(double workDays) {
        this.workDays = workDays;
    }


    public boolean isRequestShouldWorkDays() {
        return workDays < 0;
    }
}
