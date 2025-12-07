package com.example.demo;

import com.example.demo.repo.LeaveRepo;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public record LeaveCalculate(LeaveRepo leaveRepo) {


    private Set<LeaveDate> aggregateLeaveDaysSet(List<LeaveDate> leaveDates) {
        return leaveDates.stream()
                .flatMap(leaveDate -> leaveDate.getLeaveDaySet().stream())
                .collect(Collectors.toSet());
    }

    public double getEmployeeLeaveDays(int employeeId, int year, int month) {
        Set<LeaveDate> leaveDaySet = aggregateLeaveDaysSet(leaveRepo.findAllByEmployeeId(employeeId))
                .stream()
                .filter(leaveDate ->
                        leaveDate.from().getYear() == year && leaveDate.from().getMonthValue() == month
                ).collect(Collectors.toSet());

        double leaveDays = 0.0;

        for (LeaveDate leaveDate : leaveDaySet) {
            if (leaveDate.leaveType() == LeaveType.PERSONAL) {
                leaveDays++;
            } else if (leaveDate.leaveType() == LeaveType.SICK ){
                leaveDays += 0.5;
            }
        }

        return leaveDays;
    }

}
