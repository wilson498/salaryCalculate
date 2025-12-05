package com.example.demo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SalaryRepo {


    private Map<Integer, SalaryDto> salaryDtos=new HashMap<>();

    public SalaryDto findByEmployeeId(int employeeId) {
        return salaryDtos.getOrDefault(employeeId, new SalaryDto());
    }

    public void save(int id,SalaryDto salaryDto) {
        salaryDtos.putIfAbsent(id, salaryDto);
    }
}
