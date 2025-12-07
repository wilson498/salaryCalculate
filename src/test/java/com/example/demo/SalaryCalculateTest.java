package com.example.demo;

import com.example.demo.repo.LeaveRepo;
import com.example.demo.repo.SalaryRepo;
import org.junit.jupiter.api.*;

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

    private void givenEmployeeLeave(List<LeaveDate> leaveDtos) {
        leaveRepo.save(1, leaveDtos);
    }

    private void givenEmployeeSalary(int salary, SalaryType salaryType) {
        Salary salaryDto = new Salary(salary, salaryType);
        salaryRepo.save(1, salaryDto);
    }

    private LeaveDate createLeaveDto(LocalDate from, LocalDate to, LeaveType leaveType) {
        return new LeaveDate(from, to, leaveType);
    }

    @Nested
    @Tag("monthly-salary")
    class MonthlySalaryTests {

        @Test
        @Tag("basic")
        public void when_all_year_no_leave_then_salary_is_same() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of());
            int salary = salaryCalculate.calculate(1, 2025, 12);

            Assertions.assertEquals(31_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_all_month_leave_then_salary_is_zero() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 31), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);

            Assertions.assertEquals(0, salary);
        }

        @Test
        @Tag("basic")
        public void when_one_day_leave_then_salary_minus_1000() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_three_day_leave_and_not_same_leave_day_then_salary_minus_3000() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.PERSONAL
                    ),
                    createLeaveDto(
                            LocalDate.of(2025, 12, 6),
                            LocalDate.of(2025, 12, 7), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(28_000, salary);
        }

        @Nested
        @Tag("edge-case")
        class EdgeCaseTests {

            @Test
            public void when_leave_day_before_to_target_month_then_same_salary() {
                givenEmployeeSalary(31_000, SalaryType.MONTHLY);
                givenEmployeeLeave(List.of(
                        createLeaveDto(
                                LocalDate.of(2025, 11, 1),
                                LocalDate.of(2025, 11, 30), LeaveType.PERSONAL
                        )
                ));

                int salary = salaryCalculate.calculate(1, 2025, 12);

                Assertions.assertEquals(31_000, salary);
            }

            @Test
            public void when_leave_day_after_to_target_month_then_same_salary() {
                givenEmployeeSalary(31_000, SalaryType.MONTHLY);
                givenEmployeeLeave(List.of(
                        createLeaveDto(
                                LocalDate.of(2026, 1, 1),
                                LocalDate.of(2026, 1, 30), LeaveType.PERSONAL
                        )
                ));

                int salary = salaryCalculate.calculate(1, 2025, 12);

                Assertions.assertEquals(31_000, salary);
            }

            @Test
            public void when_leave_day_between_target_month_then_salary_is_zero() {
                givenEmployeeSalary(31_000, SalaryType.MONTHLY);
                givenEmployeeLeave(List.of(
                        createLeaveDto(
                                LocalDate.of(2025, 11, 1),
                                LocalDate.of(2026, 1, 31), LeaveType.PERSONAL
                        )
                ));
                int salary = salaryCalculate.calculate(1, 2025, 12);

                Assertions.assertEquals(0, salary);
            }
        }

        @Test
        @Tag("calculation")
        public void salary_is_float() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 11, 1),
                            LocalDate.of(2025, 11, 1), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 11);
            // 31000/30 = 1033.33333333~~~~~
            // 31000 -1033.3333333=29966.6667 無條件捨去29966
            Assertions.assertEquals(29_966, salary);
        }
    }

    @Nested
    @Tag("daily-salary")
    class DailySalaryTests {

        @Test
        @Tag("basic")
        public void when_salary_type_is_daily_and_no_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000, SalaryType.DAILY);
            givenEmployeeLeave(List.of());
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(31_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_daily_and_all_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000, SalaryType.DAILY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 31), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(0, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_daily_and_one_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000, SalaryType.DAILY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_000, salary);
        }
    }

    @Nested
    @Tag("hourly-salary")
    class HourlySalaryTests {

        @Test
        @Tag("basic")
        public void when_salary_type_is_hourly_and_no_leave_day_then_salary_is_same() {
            givenEmployeeSalary(125, SalaryType.HOURLY);
            givenEmployeeLeave(List.of());
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(31_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_hourly_and_all_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(125, SalaryType.HOURLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 31), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(0, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_hours_and_one_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(125, SalaryType.HOURLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_000, salary);
        }
    }

    @Nested
    @Tag("weekly-salary")
    class WeeklySalaryTests {

        @Test
        @Tag("basic")
        public void when_salary_type_is_hourly_and_no_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000 * 7, SalaryType.WEEKLY);
            givenEmployeeLeave(List.of());
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(31_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_hourly_and_all_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000 * 7, SalaryType.WEEKLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 31), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(0, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_hours_and_one_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000 * 7, SalaryType.WEEKLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_000, salary);
        }
    }

    @Nested
    @Tag("fortnightly-salary")
    class FortnightlySalaryTests {

        @Test
        @Tag("basic")
        public void when_salary_type_is_hourly_and_no_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000 * 14, SalaryType.FORTNIGHTLY);
            givenEmployeeLeave(List.of());
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(31_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_hourly_and_all_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000 * 14, SalaryType.FORTNIGHTLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 31), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(0, salary);
        }

        @Test
        @Tag("basic")
        public void when_salary_type_is_hours_and_one_month_leave_day_then_salary_is_same() {
            givenEmployeeSalary(1000 * 14, SalaryType.FORTNIGHTLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.PERSONAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_000, salary);
        }
    }
    @Nested
    @Tag("monthly-salary")
    class LeaveTypeTests {

        @Test
        @Tag("basic")
        public void when_all_year_no_leave_then_salary_is_same() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of());
            int salary = salaryCalculate.calculate(1, 2025, 12);

            Assertions.assertEquals(31_000, salary);
        }

        @Test
        @Tag("basic")
        public void when_all_month_leave_then_salary_is_zero() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 31), LeaveType.SICK
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);

            Assertions.assertEquals(15_500, salary);
        }

        @Test
        @Tag("basic")
        public void when_one_day_leave_then_salary_minus_1000() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.SICK
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_500, salary);
        }

        @Test
        @Tag("basic")
        public void when_three_day_leave_and_not_same_leave_day_then_salary_minus_3000() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 12, 1),
                            LocalDate.of(2025, 12, 1), LeaveType.SICK
                    ),
                    createLeaveDto(
                            LocalDate.of(2025, 12, 6),
                            LocalDate.of(2025, 12, 7), LeaveType.SPECIAL
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 12);
            Assertions.assertEquals(30_500, salary);
        }

        @Nested
        @Tag("edge-case")
        class EdgeCaseTests {

            @Test
            public void when_leave_day_before_to_target_month_then_same_salary() {
                givenEmployeeSalary(31_000, SalaryType.MONTHLY);
                givenEmployeeLeave(List.of(
                        createLeaveDto(
                                LocalDate.of(2025, 11, 1),
                                LocalDate.of(2025, 11, 30), LeaveType.SICK
                        )
                ));

                int salary = salaryCalculate.calculate(1, 2025, 12);

                Assertions.assertEquals(31_000, salary);
            }

            @Test
            public void when_leave_day_after_to_target_month_then_same_salary() {
                givenEmployeeSalary(31_000, SalaryType.MONTHLY);
                givenEmployeeLeave(List.of(
                        createLeaveDto(
                                LocalDate.of(2026, 1, 1),
                                LocalDate.of(2026, 1, 30), LeaveType.SICK
                        )
                ));

                int salary = salaryCalculate.calculate(1, 2025, 12);

                Assertions.assertEquals(31_000, salary);
            }

            @Test
            public void when_leave_day_between_target_month_then_salary_is_zero() {
                givenEmployeeSalary(31_000, SalaryType.MONTHLY);
                givenEmployeeLeave(List.of(
                        createLeaveDto(
                                LocalDate.of(2025, 11, 1),
                                LocalDate.of(2026, 1, 31), LeaveType.SICK
                        )
                ));
                int salary = salaryCalculate.calculate(1, 2025, 12);

                Assertions.assertEquals(15_500, salary);
            }
        }

        @Test
        @Tag("calculation")
        public void salary_is_float() {
            givenEmployeeSalary(31_000, SalaryType.MONTHLY);
            givenEmployeeLeave(List.of(
                    createLeaveDto(
                            LocalDate.of(2025, 11, 1),
                            LocalDate.of(2025, 11, 1), LeaveType.SICK
                    )
            ));
            int salary = salaryCalculate.calculate(1, 2025, 11);
            // 31000/30 = 1033.33333333~~~~~
            // 31000 - (1033.3333333)/2 = 30483.3333335 無條件捨去30483

            Assertions.assertEquals(30_483, salary);
        }
    }
}
