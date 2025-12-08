package com.example.demo;

import static com.example.demo.SalaryCalculate.getShouldWorkDays;

public record Salary(int value, SalaryType salaryType) {


    private double getDailySalary(double shouldWorkDays) {
        return (double) value / shouldWorkDays;
    }

    public double getDailySalary(int year, int month) {
        double shouldWorkDays = getShouldWorkDays(year, month);

        if (!salaryType.shouldCalculateWorkDays()) {
            shouldWorkDays = salaryType.getWorkDays();
        }
        return getDailySalary(shouldWorkDays);
    }
}
