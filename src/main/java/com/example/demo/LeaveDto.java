package com.example.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public record LeaveDto(LocalDate from, LocalDate to) {


    public Set<String> getSetLeaveDaySet() {
        Set<String> leaveDaySet = new HashSet<>();
        long days = ChronoUnit.DAYS.between(from ,to) + 1;
        for (int day = 0; day < days; day++) {
            leaveDaySet.add(from.plusDays(day).toString());
        }
        return leaveDaySet;
    }
}
