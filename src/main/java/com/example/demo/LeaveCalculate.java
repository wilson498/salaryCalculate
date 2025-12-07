package com.example.demo;

import com.example.demo.repo.LeaveRepo;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
public record LeaveCalculate(LeaveRepo leaveRepo) {


    private Set<LeaveDate> aggregateLeaveDaysSet(List<LeaveDate> leaveDates) {
        return leaveDates.stream()
                .flatMap(leaveDate -> leaveDate.getLeaveDateSet().stream())
                .collect(Collectors.toSet());
    }

    public double getEmployeeLeaveDays(int employeeId, int year, int month) {
        Set<LeaveDate> leaveDateSet = aggregateLeaveDaysSet(leaveRepo.findAllByEmployeeId(employeeId));
        Set<LeaveDate> filterLeaveDateSet = getFilterLeaveDateSet(year, month, leaveDateSet);

        double leaveDays = 0.0;

        for (LeaveDate leaveDate : filterLeaveDateSet) {
            leaveDays += getLeaveDays(leaveDate);
        }

        return leaveDays;
    }

    private Set<LeaveDate> getFilterLeaveDateSet(int year, int month, Set<LeaveDate> leaveDateSet) {
        return leaveDateSet.stream()
                .filter(leaveDate ->
                        leaveDate.from().getYear() == year && leaveDate.from().getMonthValue() == month
                ).collect(Collectors.toSet());
    }

    private double getLeaveDays(LeaveDate leaveDate) {
        if (leaveDate.leaveType() == LeaveType.PERSONAL) {
            return 1;
        }
        if (leaveDate.leaveType() == LeaveType.SICK) {
            return .5;
        }
        if (leaveDate.leaveType() == LeaveType.SPECIAL) {
            return 0;
        }
        throw new IllegalArgumentException("Unknown leave type");
    }

}
