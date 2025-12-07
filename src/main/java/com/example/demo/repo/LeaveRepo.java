package com.example.demo.repo;

import com.example.demo.LeaveDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveRepo {

    private final Map<Integer, List<LeaveDate>> leaveDateMap = new HashMap<>();

    public List<LeaveDate> findAllByEmployeeId(int employeeId) {
        return leaveDateMap.getOrDefault(employeeId, new ArrayList<>());
    }

    public void save(int employeeId, List<LeaveDate> leaveDates) {
        leaveDateMap.put(employeeId, leaveDates);
    }
}
