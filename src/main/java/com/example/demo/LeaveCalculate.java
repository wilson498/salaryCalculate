package com.example.demo;

import com.example.demo.repo.LeaveRepo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveCalculate {

    private final LeaveRepo leaveRepo;

    public LeaveCalculate(LeaveRepo leaveRepo) {
        this.leaveRepo = leaveRepo;
    }

    private Map<String, Integer> aggregateLeaveDaysByMonth(List<LeaveDate> leaveDates) {
        return leaveDates.stream()
                .flatMap(leaveDate -> leaveDate.getLeaveDayMap().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));
    }

    public int getEmployeeLeaveDays(int employeeId, int year, int month) {
        Map<String, Integer> leaveDayMapByMonth = aggregateLeaveDaysByMonth(leaveRepo.findAllByEmployeeId(employeeId));
        LocalDate date = LocalDate.of(year, month, 1);
        return leaveDayMapByMonth.getOrDefault(LeaveDate.getMonthKey(date), 0);
    }
}
