package com.example.rqchallenge.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Employee {
    private String id;
    private String employeeName;
    private String employeeSalary;
    private String employeeAge;
    private String profileImage;
}
