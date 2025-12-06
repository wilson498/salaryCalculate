package com.example.demo;

import com.example.demo.repo.LeaveRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public record LeaveCalculate(LeaveRepo leaveRepo) {


    private Set<String> aggregateLeaveDaysSet(List<LeaveDate> leaveDates) {
        return leaveDates.stream()
                .flatMap(leaveDate -> leaveDate.getLeaveDaySet().stream())
                .collect(Collectors.toSet());
    }

    public int getEmployeeLeaveDays(int employeeId, int year, int month) {
        Set<String> leaveDaySet = aggregateLeaveDaysSet(leaveRepo.findAllByEmployeeId(employeeId));
        LocalDate date = LocalDate.of(year, month, 1);
        int leaveDaysInMonth = date.lengthOfMonth();
        int leaveDays = 0;
        for (int day = 0; day < leaveDaysInMonth; day++) {
            String daysKey = LeaveDate.getDaysKey(date.plusDays(day));
            if (leaveDaySet.contains(daysKey)) {
                leaveDays++;
            }
        }
        return leaveDays;
    }

}
