package com.example.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public record LeaveDate(LocalDate from, LocalDate to) {

    public static String getDaysKey(LocalDate currentProcessDate) {
        return currentProcessDate.getYear() + "-" + currentProcessDate.getMonthValue() + "-" + currentProcessDate.getDayOfMonth();
    }

    public Set<String> getLeaveDaySet() {
        Set<String> leaveDaySet = new HashSet<>();
        long totalDays = getTotalDays();
        for (int day = 0; day < totalDays; day++) {
            LocalDate currentProcessDate = from.plusDays(day);
            leaveDaySet.add(getDaysKey(currentProcessDate));
        }
        return leaveDaySet;
    }


    private long getTotalDays() {
        return ChronoUnit.DAYS.between(from, to) + 1;
    }
}
