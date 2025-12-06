package com.example.demo;

import com.example.demo.repo.LeaveRepo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveCalculate {

    private final Map<String, Integer> leaveDayMapByMonth = new HashMap<>();
    private final LeaveRepo leaveRepo = new LeaveRepo();


    public int getLeaveDays(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        String monthKey = date.getYear() + "-" + date.getMonthValue();
        return leaveDayMapByMonth.getOrDefault(monthKey, 0);
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

    int getEmployeeLeaveDays(int employeeId, int year, int month, SalaryCalculate salaryCalculate) {
        leaveDayMapByMonth.clear();
        leaveDayMapByMonth.putAll(aggregateLeaveDaysByMonth(leaveRepo.findAllByEmployeeId(employeeId)));
        return getLeaveDays(year, month);
    }
}
