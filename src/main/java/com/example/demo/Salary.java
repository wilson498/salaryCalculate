package com.example.demo;

public record Salary(int value, SalaryType salaryType) {

    public double getDailySalary(double shouldWorkDays) {
        return salaryType.getDailySalary(value, shouldWorkDays);
    }
}
