package com.example.demo;

public record Salary(int value, SalaryType salaryType) {

    public int getSalaryActual(long monthDays, int leaveDays) {
        if (monthDays == 0) {
            return 0;
        }
        double dailySalary = (double) getSalaryBase() / monthDays;
        double deductedAmount = dailySalary * leaveDays;


        return (int) (dailySalary - deductedAmount);
    }

    private int getSalaryBase() {
        return value;
    }
}
