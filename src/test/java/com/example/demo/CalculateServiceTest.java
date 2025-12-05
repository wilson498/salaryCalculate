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

    @Test
    public void 全年沒請假() {
        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of());
        int salary = calculateService.calculate(1, 2025, 12);

        Assertions.assertEquals(31000, salary);
    }


    @Test
    public void 請了一整個月的假() {

        createLeaveDto(LocalDate.of(2025,12,1),LocalDate.of(2025,12,31));


        givenEmployeeSalary(1, 31000);
        givenEmployeeLeave(1, List.of());
        int salary = calculateService.calculate(1, 2025, 21);

        Assertions.assertEquals(0, salary);
    }

    private void createLeaveDto(LocalDate from, LocalDate to) {
        LeaveDto leaveDto = new LeaveDto();
        leaveDto.setFrom(from);
        leaveDto.setTo(to);
    }

    @Test
    public void 當月請了一天的假() {
        int salary = calculateService.calculate(1, 2025, 12);

        Assertions.assertEquals(30000, salary);
    }

    @Test
    public void 全年有請假_但沒12沒有在內() {
        int salary = calculateService.calculate(1, 2025, 12);

        Assertions.assertEquals(31000, salary);
    }
}