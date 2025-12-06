package com.example.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;


public record LeaveDate(LocalDate from, LocalDate to) {


    public Map<String, Integer> getLeaveDayMap() {
        Map<String, Integer> leaveDayMap = new HashMap<>();
        long days = ChronoUnit.DAYS.between(from, to) + 1;
        for (int day = 0; day < days; day++) {
            LocalDate currentProcessDate = from.plusDays(day);
            String key = currentProcessDate.getYear() + "-" + currentProcessDate.getMonthValue();
            int sum = leaveDayMap.getOrDefault(key, 0);
            leaveDayMap.put(key, ++sum);
        }
        return leaveDayMap;
    }
}
