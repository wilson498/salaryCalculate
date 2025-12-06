package com.example.demo;

import com.example.demo.repo.LeaveRepo;
import com.example.demo.repo.SalaryRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;


class SalaryCalculateTest {

    private SalaryCalculate salaryCalculate;
    private SalaryRepo salaryRepo;
    private LeaveRepo leaveRepo;


    @BeforeEach
    void setUp() {
        salaryRepo = new SalaryRepo();
        leaveRepo = new LeaveRepo();
        LeaveCalculate leaveCalculate = new LeaveCalculate(leaveRepo);
        salaryCalculate = new SalaryCalculate(salaryRepo, leaveCalculate);
    }

    private void givenEmployeeLeave(int id, List<LeaveDate> leaveDtos) {
        leaveRepo.save(id, leaveDtos);
    }

    private void givenEmployeeSalary(int id, int salary) {
        Salary salaryDto = new Salary(salary);
        salaryRepo.save(id, salaryDto);
    }

    private LeaveDate createLeaveDto(LocalDate from, LocalDate to) {
        return new LeaveDate(from, to);
    }

    @Test
    public void when_all_year_no_leave_then_salary_is_same() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of());
        int salary = salaryCalculate.calculate(1, 2025, 12);

        Assertions.assertEquals(31000, salary);
    }

    @Test
    public void when_all_month_leave_then_salary_is_zero() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2025, 12, 1),
                        LocalDate.of(2025, 12, 31)
                )
        ));
        int salary = salaryCalculate.calculate(1, 2025, 12);

        Assertions.assertEquals(0, salary);
    }


    @Test
    public void when_one_day_leave_then_salary_minus_1000() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2025, 12, 1),
                        LocalDate.of(2025, 12, 1)
                )
        ));
        int salary = salaryCalculate.calculate(1, 2025, 12);
        Assertions.assertEquals(30000, salary);
    }


    @Test
    public void when_three_day_leave_and_not_same_leave_day_then_salary_minus_3000() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2025, 12, 1),
                        LocalDate.of(2025, 12, 1)
                ),
                createLeaveDto(
                        LocalDate.of(2025, 12, 6),
                        LocalDate.of(2025, 12, 7)
                )
        ));
        int salary = salaryCalculate.calculate(1, 2025, 12);
        Assertions.assertEquals(28000, salary);
    }

    @Test
    public void when_leave_day_before_to_target_month_then_same_salary() {

        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2025, 11, 1),
                        LocalDate.of(2025, 11, 30)
                )
        ));

        int salary = salaryCalculate.calculate(1, 2025, 12);

        Assertions.assertEquals(31000, salary);
    }

    @Test
    public void when_leave_day_after_to_target_month_then_same_salary() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 1, 30)
                )
        ));

        int salary = salaryCalculate.calculate(1, 2025, 12);

        Assertions.assertEquals(31000, salary);
    }


    @Test
    public void when_leave_day_between_target_month_then_salary_is_zero() {

        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2025, 11, 1),
                        LocalDate.of(2026, 1, 31)
                )
        ));
        int salary = salaryCalculate.calculate(1, 2025, 12);

        Assertions.assertEquals(0, salary);
    }

    @Test
    public void salary_is_float() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of(
                createLeaveDto(
                        LocalDate.of(2025, 11, 1),
                        LocalDate.of(2025, 11, 1)
                )
        ));
        int salary = salaryCalculate.calculate(1, 2025, 11);
        // 31000/30 = 1033.33333333~~~~~
        // 31000 -1033.3333333=29966.6667 無條件捨去29966
        Assertions.assertEquals(29966, salary);
    }

}