package com.example.demo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record LeaveDate(LocalDate from, LocalDate to, LeaveType leaveType) {

    public Set<LeaveDate> getLeaveDateSet() {
        Set<LeaveDate> leaveDateSet = new HashSet<>();
        if (!isInRange(from)) return leaveDateSet;
        int dayOffset = 0;
        LocalDate currentDate = from.plusDays(dayOffset);
        do {
            leaveDateSet.add(new LeaveDate(currentDate, currentDate, leaveType));
            currentDate = from.plusDays(++dayOffset);
        } while (isInRange(currentDate));
        return leaveDateSet;
    }

    private boolean isInRange(LocalDate dateToCheck) {
        return !dateToCheck.isAfter(to);
    }


}
