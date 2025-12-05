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
        Set<String> leaveDaySet = getSetLeaveDaySet(leaveDtos);

        LocalDate startMonth = LocalDate.of(year, month, 1);
        LocalDate endMonth = startMonth.withDayOfMonth(startMonth.lengthOfMonth());
        Leave result = getLeave(startMonth, endMonth, leaveDaySet);

        return salary - (salary / result.monthDays()) * result.leaveDays();
    }

    private Leave getLeave(LocalDate startMonth, LocalDate endMonth, Set<String> leaveDaySet) {
        int leaveDays = 0;
        int monthDays = (int) ChronoUnit.DAYS.between(startMonth, endMonth) + 1;
        for (int day = 0; day < monthDays; day++) {
            LocalDate date = startMonth.plusDays(day);
            if (leaveDaySet.contains(date.toString())) {
                leaveDays++;
            }
        }
        Leave result = new Leave(leaveDays, monthDays);
        return result;
    }

    private record Leave(int leaveDays, int monthDays) {
    }

    private Set<String> getSetLeaveDaySet(List<LeaveDto> leaveDtos) {
        Set<String> leaveDaySet = new HashSet<>();

        for (LeaveDto leaveDto : leaveDtos) {
            long days = ChronoUnit.DAYS.between(leaveDto.getFrom(), leaveDto.getTo()) + 1;
            for (int day = 0; day < days; day++) {
                leaveDaySet.add(leaveDto.getFrom().plusDays(day).toString());
            }
        }
        return leaveDaySet;
    }
}
