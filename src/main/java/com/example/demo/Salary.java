package com.example.demo;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import static com.example.demo.SalaryCalculate.getShouldWorkDays;

@Data
@RequiredArgsConstructor
public class Salary {
    private final int value;
    private final SalaryType salaryType;

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
