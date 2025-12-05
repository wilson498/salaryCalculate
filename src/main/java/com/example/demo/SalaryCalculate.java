package com.example.demo;


import com.example.demo.repo.LeaveRepo;
import com.example.demo.repo.SalaryRepo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class SalaryCalculate {
    private final SalaryRepo salaryRepo;
    private final LeaveRepo leaveRepo;

    public int calculate(int employeeId, int year, int month) {
        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        long monthDays = ChronoUnit.DAYS.between(startMonth, endMonth) + 1;

        Salary salary = salaryRepo.findByEmployeeId(employeeId);
        LeaveCalculate leaveCalculate = new LeaveCalculate(leaveRepo.findAllByEmployeeId(employeeId));

        int leaveDays = leaveCalculate.getLeaveDays(monthDays, startMonth);

        return salary.getSalaryActual(monthDays, leaveDays);
    }


}
