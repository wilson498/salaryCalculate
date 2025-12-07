package com.example.demo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record LeaveDate(LocalDate from, LocalDate to, LeaveType leaveType) {

    public Set<LeaveDate> getLeaveDaySet() {
        Set<LeaveDate> leaveDaySet = new HashSet<>();
        if (!isInRange(from)) return leaveDaySet;

        int day = 0;
        LocalDate currentProcessDate = from.plusDays(day);
        do {
            leaveDaySet.add(new LeaveDate(currentProcessDate, currentProcessDate, leaveType));
            currentProcessDate = from.plusDays(++day);
        } while (isInRange(currentProcessDate));
        return leaveDaySet;
    }

    private boolean isInRange(LocalDate fromPlusDay) {
        return !fromPlusDay.isAfter(to);
    }


}
