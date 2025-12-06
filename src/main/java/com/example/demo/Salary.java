package com.example.demo;

public record Salary(int value, SalaryType salaryType) {

    public int getSalaryActual(long shouldWorkDays, int leaveDays) {
        if (shouldWorkDays == 0) {
            return 0;
        }
        double dailySalary = getDailySalary(shouldWorkDays);
        return (int) (dailySalary * (shouldWorkDays - leaveDays));
    }

    private double getDailySalary(long shouldWorkDays) {
        if (salaryType == SalaryType.DAILY) return value;
        return (double) value / shouldWorkDays;
    }
}
