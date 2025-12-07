package com.example.demo;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public record LeaveDate(LocalDate from, LocalDate to) {

    public static String getDateKey(LocalDate currentProcessDate) {
        return currentProcessDate.getYear() + "-" + currentProcessDate.getMonthValue() + "-" + currentProcessDate.getDayOfMonth();
    }

    public Set<String> getLeaveDaySet() {
        Set<String> leaveDaySet = new HashSet<>();
        int day = 0;
        do {
            LocalDate currentProcessDate = from.plusDays(day);
            leaveDaySet.add(getDateKey(currentProcessDate));
        } while (isInRange(from.plusDays(++day)));
        return leaveDaySet;
    }

    private boolean isInRange(LocalDate fromPlusDay) {
        return !fromPlusDay.isAfter(to);
    }


}
