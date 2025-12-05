package com.example.demo;


import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@RequiredArgsConstructor
public class CalculateService {

    private final SalaryRepo salaryRepo;
    private final LeaveRepo leaveRepo;

    public int calculate(int employeeId, int year, int month) {

        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        long monthDays = ChronoUnit.DAYS.between(startMonth, endMonth) + 1;

        int salary = salaryRepo.findByEmployeeId(employeeId).getValue();
        List<LeaveDate> leaveDates = leaveRepo.findAllByEmployeeId(employeeId);


        Set<String> leaveDaySet = new HashSet<>();
        for (LeaveDate leaveDate : leaveDates) {
            leaveDaySet.addAll(leaveDate.getSetLeaveDaySet());
        }

        int leaveDays = getLeaveDays(monthDays, startMonth, leaveDaySet);

        return (int) (salary - (salary / monthDays) * leaveDays);
    }

    private int getLeaveDays(long monthDays, LocalDate startMonth, Set<String> leaveDaySet) {
        int leaveDays = 0;
        for (int day = 0; day < monthDays; day++) {
            LocalDate date = startMonth.plusDays(day);
            if (leaveDaySet.contains(date.toString())) {
                leaveDays++;
            }
        }
        return leaveDays;
    }

}
