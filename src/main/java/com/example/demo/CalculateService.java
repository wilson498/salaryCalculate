package com.example.demo;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class CalculateService {

    @Setter
    private SalaryRepo salaryRepo;
    @Setter
    private LeaveRepo leaveRepo;

    public int calculate(int employeeId, int year, int month) {


        int salary = salaryRepo.findByEmployeeId(employeeId).getValue();

        List<LeaveDto> leaveDtos = leaveRepo.findAllByEmployeeId(employeeId);
        Set<String> leaveDaySet = new HashSet<>();
        for(LeaveDto leaveDto : leaveDtos) {
            leaveDaySet.addAll(leaveDto.getSetLeaveDaySet());
        }

        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        int leaveDays = 0;
        int monthDays = (int) ChronoUnit.DAYS.between(startMonth, endMonth) + 1;
        for (int day = 0; day < monthDays; day++) {
            LocalDate date = startMonth.plusDays(day);
            if (leaveDaySet.contains(date.toString())) {
                leaveDays++;
            }
        }

        return salary - (salary / monthDays) * leaveDays;
    }

}
