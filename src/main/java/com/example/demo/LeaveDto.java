package com.example.demo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveDto {
    private LocalDate from;
    private LocalDate to;
}
