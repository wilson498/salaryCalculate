package com.example.demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveCalculate {

    private final List<LeaveDate> leaveDates;
    private final Map<String, Integer> leaveDayMap;

    LeaveCalculate(List<LeaveDate> leaveDates) {
        this.leaveDates = leaveDates;
        leaveDayMap = getLeaveDayMap();
    }

    private Map<String, Integer> getLeaveDayMap() {
        return leaveDates.stream().collect(
                HashMap::new,
                (map, leaveDate) -> leaveDate.getLeaveDayMap().forEach(
                        (key, value) -> {
                            int sum = map.getOrDefault(key, 0);
                            map.put(key, sum + value);
                        }
                ),
                HashMap::putAll);
    }

    public int getLeaveDays(int year, int month) {
        return leaveDayMap.getOrDefault(year + "-" + month, 0);
    }
}
