package com.example.demo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record LeaveCalculate(List<LeaveDate> leaveDates) {

    private Set<String> getStringSet() {
        Set<String> leaveDaySet = new HashSet<>();
        for (LeaveDate leaveDate : leaveDates) {
            leaveDaySet.addAll(leaveDate.getSetLeaveDaySet());
        }
        return leaveDaySet;
    }

    public int getLeaveDays(long monthDays, LocalDate startDate) {
        Set<String> leaveDaySet = getStringSet();
        int leaveDays = 0;
        for (int day = 0; day < monthDays; day++) {
            LocalDate date = startDate.plusDays(day);
            if (leaveDaySet.contains(date.toString())) {
                leaveDays++;
            }
        }
        return leaveDays;
    }
}
