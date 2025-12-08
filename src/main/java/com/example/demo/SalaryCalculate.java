package com.example.demo;

import com.example.demo.repo.LeaveRepo;
import com.example.demo.repo.SalaryRepo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public record SalaryCalculate(SalaryRepo salaryRepo, LeaveRepo leaveRepo) {

    public static long getShouldWorkDays(int year, int month) {
        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        return ChronoUnit.DAYS.between(startMonth, endMonth) + 1;
    }

    public int calculate(int employeeId, int year, int month) {
        Salary salary = salaryRepo.findByEmployeeId(employeeId);
        LeaveCalculate leaveCalculate = new LeaveCalculate(leaveRepo);

        double leaveDays = leaveCalculate.getEmployeeLeaveDays(employeeId, year, month);
        long shouldWorkDays = getShouldWorkDays(year, month);
        double dailySalary = salary.getDailySalary(year, month);
        double actualWorkDays = shouldWorkDays - leaveDays;

        return getSalaryActual(dailySalary, actualWorkDays);
    }


    private int getSalaryActual(double dailySalary, double actualWorkDays) {
        return (int) (dailySalary * actualWorkDays);
    }
}
