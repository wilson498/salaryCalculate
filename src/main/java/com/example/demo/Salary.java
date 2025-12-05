package com.example.demo;

public record Salary(int value) {

    public int getSalaryActual(long monthDays, int leaveDays) {
        return (int) (value - value * leaveDays / monthDays);
    }
}
