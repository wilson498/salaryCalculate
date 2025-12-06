package com.example.demo;

public record Salary(int value) {

    public int getSalaryActual(long monthDays, int leaveDays) {
        if (monthDays == 0) {
            return 0;
        }
        double dailySalary = (double) value / monthDays;
        double deductedAmount = dailySalary * leaveDays;
        return (int) (value - deductedAmount);
    }
}
