package com.example.demo;

import com.example.demo.repo.SalaryRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record SalaryCalculate(SalaryRepo salaryRepo, LeaveCalculate leaveCalculate) {

    public int calculate(int employeeId, int year, int month) {
        Salary salary = salaryRepo.findByEmployeeId(employeeId);
        int leaveDays = leaveCalculate.getEmployeeLeaveDays(employeeId, year, month);
        long shouldWorkDays = getShouldWorkDays(year, month);

        return salary.getSalaryActual(shouldWorkDays, leaveDays);
    }

    private long getShouldWorkDays(int year, int month) {
        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        return ChronoUnit.DAYS.between(startMonth, endMonth) + 1;
    }

}
