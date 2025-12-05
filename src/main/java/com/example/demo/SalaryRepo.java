package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SalaryRepo {


    private Map<Integer, Salary> salaryDtos=new HashMap<>();

    public Salary findByEmployeeId(int employeeId) {
        return salaryDtos.getOrDefault(employeeId, new Salary());
    }

    public void save(int id, Salary salary) {
        salaryDtos.putIfAbsent(id, salary);
    }
}
