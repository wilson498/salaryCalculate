package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LeaveRepo {

    private Map<Integer, List<LeaveDate>> leaveDtoMap = new HashMap<>();

    public List<LeaveDate> findAllByEmployeeId(int employeeId) {
        return leaveDtoMap.getOrDefault(employeeId, null);
    }

    public void save(int i, List<LeaveDate> leaveDate) {
        leaveDtoMap.put(i, leaveDate);
    }
}
