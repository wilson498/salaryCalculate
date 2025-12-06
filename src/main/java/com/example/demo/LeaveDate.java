package com.example.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public record LeaveDate(LocalDate from, LocalDate to) {


    public static String getMonthKey(LocalDate currentDate) {
        return currentDate.getYear() + "-" + currentDate.getMonthValue();
    }

    public Map<String, Integer> getLeaveDayMap() {
        Map<String, Integer> leaveDayMap = new HashMap<>();
        long totalDays = getTotalDays();

        for (int day = 0; day < totalDays; day++) {
            LocalDate currentDate = from.plusDays(day);
            String monthKey = getMonthKey(currentDate);
            leaveDayMap.merge(monthKey, 1, Integer::sum);
        }

        return leaveDayMap;
    }


    private long getTotalDays() {
        return ChronoUnit.DAYS.between(from, to) + 1;
    }
}
