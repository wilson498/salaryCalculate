package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SalaryRepo {


    private final Map<Integer, Salary> salaryMap =new HashMap<>();

    public Salary findByEmployeeId(int employeeId) {
        return salaryMap.getOrDefault(employeeId, new Salary());
    }

    public void save(int id, Salary salary) {
        salaryMap.putIfAbsent(id, salary);
    }
}
