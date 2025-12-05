package com.example.demo;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LeaveRepo {

    private Map<Integer, List<LeaveDto>> leaveDtoMap = new HashMap<>();

    public List<LeaveDto> findAllByEmployeeId(int employeeId) {
        return leaveDtoMap.getOrDefault(employeeId, null);
    }

    public void save(int i, List<LeaveDto> leaveDtos) {
        leaveDtoMap.put(i, leaveDtos);
    }
}
