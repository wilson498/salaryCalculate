package com.example.demo;


import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Service
public class CalculateService {

    @Setter
    private SalaryRepo salaryRepo;
    @Setter
    private LeaveRepo leaveRepo;

    public int calculate(int employeeId, int year, int month) {
        int salary = salaryRepo.findByEmployeeId(employeeId).getValue();
        List<LeaveDto> leaveDtos = leaveRepo.findAllByEmployeeId(employeeId);
        Set<LocalDate> localDateSet = leaveDtos
                .stream().map(t -> {
                    LocalDate from = t.getFrom();
                    LocalDate to = t.getTo();
return from.withYear(year).withMonth(month);
                });

        return salary;
    }
}
