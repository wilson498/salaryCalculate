package com.example.demo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record LeaveDate(LocalDate from, LocalDate to, LeaveType leaveType) {

    public Set<LeaveDate> getLeaveDateSet() {
        Set<LeaveDate> leaveDateSet = new HashSet<>();

        LocalDate currentDate = from;
        while (isInRange(currentDate)) {
            leaveDateSet.add(new LeaveDate(currentDate, currentDate, leaveType));
            currentDate = currentDate.plusDays(1);
        }
        ;
        return leaveDateSet;
    }

    private boolean isInRange(LocalDate dateToCheck) {
        return !dateToCheck.isAfter(to);
    }

}
