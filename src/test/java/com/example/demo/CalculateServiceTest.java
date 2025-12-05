package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
class CalculateServiceTest {

    @Autowired
    private CalculateService calculateService;
    @Autowired
    private SalaryRepo salaryRepo;
    @Autowired
    private LeaveRepo leaveRepo;


    @BeforeEach
    void setUp() {
        salaryRepo = new SalaryRepo();
        leaveRepo = new LeaveRepo();
        calculateService.setSalaryRepo(salaryRepo);
        calculateService.setLeaveRepo(leaveRepo);
    }

    private void givenEmployeeLeave(int id, List<LeaveDto> leaveDtos) {
        leaveRepo.save(id, leaveDtos);
    }

    private void givenEmployeeSalary(int id, int salary) {
        SalaryDto salaryDto = new SalaryDto();
        salaryDto.setValue(salary);
        salaryRepo.save(id, salaryDto);
    }

    private LeaveDto createLeaveDto(LocalDate from, LocalDate to) {
        LeaveDto leaveDto = new LeaveDto();
        leaveDto.setFrom(from);
        leaveDto.setTo(to);
        return leaveDto;
    }

    @Test
    public void 全年沒請假() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of());
        int salary = calculateService.calculate(1, 2025, 12);

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
        int salary = calculateService.calculate(1, 2025, 12);

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
        int salary = calculateService.calculate(1, 2025, 12);
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
        int salary = calculateService.calculate(1, 2025, 12);
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

        int salary = calculateService.calculate(1, 2025, 12);

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

        int salary = calculateService.calculate(1, 2025, 12);

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

        int salary = calculateService.calculate(1, 2025, 12);

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
        int salary = calculateService.calculate(1, 2025, 12);

        Assertions.assertEquals(0, salary);
    }

}