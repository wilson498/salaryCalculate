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

        if (salaryType == SalaryType.HOURLY) {
            return (double) value * 8;
        }

        return value;
    }
}
