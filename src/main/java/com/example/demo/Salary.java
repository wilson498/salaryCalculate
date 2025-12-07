package com.example.demo;

public record Salary(int value, SalaryType salaryType) {

    public int getSalaryActual(long shouldWorkDays, int leaveDays) {
        if (shouldWorkDays == 0) {
            return 0;
        }

        double dailySalary = getDailySalary(shouldWorkDays);
        long actualWorkDays = shouldWorkDays - leaveDays;

        return (int) (dailySalary * actualWorkDays);
    }

    private double getDailySalary(long shouldWorkDays) {
        if (salaryType == SalaryType.MONTHLY) {
            return (double) value / shouldWorkDays;
        }
        if (salaryType == SalaryType.WEEKLY) {
            return (double) value / 7;
        }
        if (salaryType == SalaryType.FORTNIGHTLY) {
            return (double) value / 14;
        }
        if (salaryType == SalaryType.HOURLY) {
            return (double) value / ((double) 1 / 8);
        }
        return value;
    }
}
