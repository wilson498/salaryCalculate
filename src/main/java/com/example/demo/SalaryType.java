package com.example.demo;

import lombok.Getter;

public enum SalaryType {
    HOURLY((double) 1 / 8),

    DAILY(1.0),

    WEEKLY(7.0),

    FORTNIGHTLY(14.0),

    MONTHLY(-1.0);

    @Getter
    private final double workDays;

    SalaryType(double workDays) {
        this.workDays = workDays;
    }


    public boolean shouldCalculateWorkDays() {
        return workDays < 0;
    }
}
