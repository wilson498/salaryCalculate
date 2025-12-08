package com.example.demo;

import com.example.demo.repo.SalaryRepo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class SalaryCalculateService {
    private final SalaryRepo salaryRepo;
    private final LeaveCalculate leaveCalculate;



    public int calculate(int employeeId, int year, int month) {
        Salary salary = salaryRepo.findByEmployeeId(employeeId);
        double leaveDays = leaveCalculate.getEmployeeLeaveDays(employeeId, year, month);
        double shouldWorkDays = monthlyWorkDays(year, month);
        double dailySalary = getDailySalary(salary, shouldWorkDays);
        double actualWorkDays = shouldWorkDays - leaveDays;

        return getSalaryActual(dailySalary, actualWorkDays);
    }

    public double monthlyWorkDays(int year, int month) {
        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        return ChronoUnit.DAYS.between(startMonth, endMonth) + 1;
    }

    public int getSalaryActual(double dailySalary, double actualWorkDays) {
        return (int) (dailySalary * actualWorkDays);
    }


    public double getDailySalary(Salary salary, double shouldWorkDays) {
        if(shouldWorkDays == 0) return 0;
        return salary.salaryType().shouldCalculateWorkDays() ?
                salary.value() / shouldWorkDays : (double) salary.value() / salary.salaryType().getWorkDays();
    }
}
