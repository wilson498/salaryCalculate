package com.example.demo;

import com.example.demo.repo.LeaveRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class LeaveCalculate {
    private final LeaveRepo leaveRepo;

    public double getEmployeeLeaveDays(int employeeId, int year, int month) {
        Set<LeaveDate> allLeaveDateSet = aggregateLeaveDaysSet(leaveRepo.findAllByEmployeeId(employeeId));
        Set<LeaveDate> monthlyLeaveDates = getFilterLeaveDateSet(year, month, allLeaveDateSet);

        double totalLeaveDays = 0.0;

        for (LeaveDate leaveDate : monthlyLeaveDates) {
            totalLeaveDays += getLeaveDays(leaveDate);
        }

        return totalLeaveDays;
    }

    private Set<LeaveDate> aggregateLeaveDaysSet(List<LeaveDate> leaveDates) {
        return leaveDates.stream()
                .flatMap(leaveDate -> leaveDate.getLeaveDateSet().stream())
                .collect(Collectors.toSet());
    }

    private Set<LeaveDate> getFilterLeaveDateSet(int year, int month, Set<LeaveDate> leaveDateSet) {
        return leaveDateSet.stream()
                .filter(leaveDate ->
                        leaveDate.from().getYear() == year && leaveDate.from().getMonthValue() == month
                ).collect(Collectors.toSet());
    }

    private double getLeaveDays(LeaveDate leaveDate) {
        return leaveDate.leaveType().getLeaveDayNum();
    }

}
