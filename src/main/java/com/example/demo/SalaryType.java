package com.example.demo;

import lombok.Getter;

public enum SalaryType {
    HOURLY((double) 1 / 8),

    DAILY(1.0),

    WEEKLY(7.0),

    FORTNIGHTLY(14.0),

    MONTHLY(-1.0);

    private final double workDays;

    SalaryType(double workDays) {
        this.workDays = workDays;
    }

    public boolean shouldCalculateWorkDays() {
        return workDays < 0;
    }

    public double getDailySalary(int value, double shouldWorkDays) {
        return value / getWorkDays(shouldWorkDays);
    }

    private double getWorkDays(double shouldWorkDays) {
        if(shouldCalculateWorkDays()) return shouldWorkDays;
        return workDays;
    }
}
