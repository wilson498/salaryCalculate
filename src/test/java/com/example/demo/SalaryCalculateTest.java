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
        salaryCalculate = new SalaryCalculate(salaryRepo, leaveRepo);
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
    public void 全年沒請假() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of());
        int salary = salaryCalculate.calculate(1, 2025, 12);

        Assertions.assertEquals(31000, salary);
    }

    @Test
    public void 請了一整個月的假() {


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
    public void 當月請了一天的假() {
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
    public void 當月請了三天的假並且是拆開的() {
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
    public void 請假時間比12月早() {

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
    public void 請假時間比12月晚() {
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
    public void 全年有請假_請假時間比較晚() {
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
    public void 請了超過整個月的假() {


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

}