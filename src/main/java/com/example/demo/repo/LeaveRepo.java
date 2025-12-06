package com.example.demo.repo;

import com.example.demo.LeaveDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaveRepo {

    private final Map<Integer, List<LeaveDate>> leaveDtoMap = new HashMap<>();

    public List<LeaveDate> findAllByEmployeeId(int employeeId) {
        return leaveDtoMap.getOrDefault(employeeId, new ArrayList<>());
    }

    public void save(int i, List<LeaveDate> leaveDate) {
        leaveDtoMap.put(i, leaveDate);
    }
}
