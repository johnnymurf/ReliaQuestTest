package com.example.rqchallenge.dtos;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
public class EmployeeResponse {
    private List<Employee> employees;
    private HttpStatus status;

}
