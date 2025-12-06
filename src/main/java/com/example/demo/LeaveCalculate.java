package com.example.demo;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveCalculate {

    private final Map<String, Integer> leaveDayMap;

    public LeaveCalculate(List<LeaveDate> leaveDates) {
        leaveDayMap = getLeaveDayMap(leaveDates);
    }

    private Map<String, Integer> getLeaveDayMap(List<LeaveDate> leaveDates) {
        return leaveDates.stream()
                .flatMap(leaveDate -> leaveDate.getLeaveDayMap().entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));
    }

    public int getLeaveDays(int year, int month) {
        LocalDate date = LocalDate.of(year, month, 1);
        return leaveDayMap.getOrDefault(date.getYear() + "-" + date.getMonthValue(), 0);
    }
}
