package com.example.demo.repo;

import com.example.demo.Salary;
import com.example.demo.SalaryType;

import java.util.HashMap;
import java.util.Map;

public class SalaryRepo {


    private final Map<Integer, Salary> salaryMap = new HashMap<>();

    public Salary findByEmployeeId(int employeeId) {
        return salaryMap.getOrDefault(employeeId, new Salary(0, SalaryType.MONTHLY));
    }

    public void save(int employeeId, Salary salary) {
        salaryMap.putIfAbsent(employeeId, salary);
    }
}
