package com.example.demo;


import com.example.demo.repo.LeaveRepo;
import com.example.demo.repo.SalaryRepo;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class SalaryCalculate {
    private final SalaryRepo salaryRepo;
    private final LeaveRepo leaveRepo;

    public int calculate(int employeeId, int year, int month) {

        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        long monthDays = ChronoUnit.DAYS.between(startMonth, endMonth) + 1;
        int salary = salaryRepo.findByEmployeeId(employeeId).value();

        List<LeaveDate> leaveDates = leaveRepo.findAllByEmployeeId(employeeId);
        LeaveCalculate leaveCalculate = new LeaveCalculate(leaveDates);


        int leaveDays = leaveCalculate.getLeaveDays(monthDays, startMonth);


        return getAnInt(salary, monthDays, leaveDays);
    }

    private int getAnInt(int salary, long monthDays, int leaveDays) {
        return (int) (salary - (salary / monthDays) * leaveDays);
    }


}
