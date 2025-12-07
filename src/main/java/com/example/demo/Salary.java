package com.example.demo;

public record Salary(int value, SalaryType salaryType) {

    public int getSalaryActual(long shouldWorkDays, double leaveDays) {
        if (shouldWorkDays == 0) {
            return 0;
        }

        double dailySalary = getDailySalary(shouldWorkDays);
        double actualWorkDays = shouldWorkDays - leaveDays;

        return (int) (dailySalary * actualWorkDays);
    }

    private double getDailySalary(long shouldWorkDays) {
        return salaryType.shouldCalculateWorkDays() ?
                (double) value / shouldWorkDays :
                value / salaryType.getWorkDays();
    }
}
