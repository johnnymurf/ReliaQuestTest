package com.example.rqchallenge.employees;

import com.example.rqchallenge.dtos.Employee;
import com.example.rqchallenge.dtos.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class EmployeeController implements IEmployeeController {

    private final RestTemplate restTemplate;
    private static final String BASE_URL = "https://dummy.restapiexample.com/api/v1";

    public EmployeeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Fetching all employees");
        try {
            ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL + "/employees", EmployeeResponse.class);
            if (response.getBody() != null && HttpStatus.OK.equals(response.getBody().getStatus())) {
                return ResponseEntity.ok(response.getBody().getEmployees());
            } else {
                log.error("Failed to fetch employees: {}", response.getBody() != null ? response.getBody().getStatus() : "Unknown error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
            }
        } catch (RestClientException e) {
            log.error("Error while fetching employees: {}", e.getMessage(), e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;  // Default to 500
            // Check if we caught an HttpStatusCodeException, which contains an actual status code
            if (e instanceof HttpStatusCodeException) {
                status = ((HttpStatusCodeException) e).getStatusCode();
            }
            return ResponseEntity.status(status).body(Collections.emptyList());
        }
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        if (searchString == null || searchString.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Searching employees with name containing: {}", searchString);

            List<Employee> allEmployees = getAllEmployees().getBody();
            if (allEmployees == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
            List<Employee> filteredEmployees = allEmployees.stream()
                    .filter(e -> e.getEmployeeName().toLowerCase().contains(searchString.toLowerCase()))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(filteredEmployees);

    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        if (id == null || id.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        if (!isValidId(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        log.info("Fetching employee with id: {}", id);
        try {
            ResponseEntity<EmployeeResponse> response = restTemplate.getForEntity(BASE_URL + "/employee/" + id, EmployeeResponse.class);
            if (response.getBody() != null && HttpStatus.OK.equals(response.getBody().getStatus())) {
                return ResponseEntity.ok(response.getBody().getEmployees().get(0));
            } else {
                log.error("Employee not found with id: {}", id);
                return ResponseEntity.notFound().build();
            }
        } catch (RestClientException e) {
            log.error("Error while fetching employees: {}", e.getMessage(), e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof HttpStatusCodeException) {
                status = ((HttpStatusCodeException) e).getStatusCode();
            }
            return ResponseEntity.status(status).build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        log.info("Fetching highest salary of employees");
            List<Employee> allEmployees = getAllEmployees().getBody();
            if (allEmployees == null || allEmployees.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            int highestSalary = allEmployees.stream()
                    .mapToInt(e -> Integer.parseInt(e.getEmployeeSalary()))
                    .max()
                    .orElse(0);
            return ResponseEntity.ok(highestSalary);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        log.info("Fetching top 10 highest earning employee names");
            List<Employee> allEmployees = getAllEmployees().getBody();
            if (allEmployees == null || allEmployees.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            List<String> topTenNames = allEmployees.stream()
                    .sorted((e1, e2) -> Integer.compare(Integer.parseInt(e2.getEmployeeSalary()), Integer.parseInt(e1.getEmployeeSalary())))
                    .limit(10)
                    .map(Employee::getEmployeeName)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(topTenNames);
        }



    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        ResponseEntity<String> validationResult = validateEmployeeInput(employeeInput);
        if (validationResult.getStatusCode() != HttpStatus.OK) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Creating new employee with input: {}", employeeInput);
        try {
            ResponseEntity<EmployeeResponse> response = restTemplate.postForEntity(BASE_URL + "/create", employeeInput, EmployeeResponse.class);
            if (response.getBody() != null && HttpStatus.OK.equals(response.getBody().getStatus())) {
                Employee createdEmployee = response.getBody().getEmployees().get(0);
                log.info("Successfully created employee: {}", createdEmployee);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
            } else {
                log.error("Failed to create employee: {}", response.getBody() != null ? response.getBody().getStatus() : "Unknown error");
                return ResponseEntity.status(response.getStatusCode()).build();
            }

        } catch (RestClientException e) {
            log.error("Error while fetching employees: {}", e.getMessage(), e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof HttpStatusCodeException) {
                status = ((HttpStatusCodeException) e).getStatusCode();
            }
            return ResponseEntity.status(status).build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        if (!isValidId(id)) {
            return ResponseEntity.badRequest().body("Invalid employee ID");
        }

        log.info("Deleting employee with id: {}", id);
        try {
            ResponseEntity<EmployeeResponse> response = restTemplate.exchange(
                    BASE_URL + "/delete/" + id,
                    HttpMethod.DELETE,
                    null,
                    EmployeeResponse.class
            );
            if (response.getBody() != null && HttpStatus.OK.equals(response.getBody().getStatus())) {
                return ResponseEntity.ok("Successfully deleted employee with id: " + id);
            } else {
                log.error("Failed to delete employee: {}", response.getBody() != null ? response.getBody().getStatus() : "Unknown error");
                return ResponseEntity.status(response.getStatusCode())
                        .body("Failed to delete employee: " + (response.getBody() != null ? response.getBody().getStatus() : "Unknown error"));
            }
        } catch (RestClientException e) {
            log.error("Error while fetching employees: {}", e.getMessage(), e);
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (e instanceof HttpStatusCodeException) {
                status = ((HttpStatusCodeException) e).getStatusCode();
            }
            return ResponseEntity.status(status).build();
        }
    }

    private ResponseEntity<String> validateEmployeeInput(Map<String, Object> employeeInput) {
        String name = (String) employeeInput.get("name");
        Object salary = employeeInput.get("salary");
        Object age = employeeInput.get("age");

        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Employee name cannot be empty");
        }

        if (salary == null || age == null) {
            return ResponseEntity.badRequest().body("Salary and age must be provided");
        }

        try {
            int salaryValue = Integer.parseInt(salary.toString());
            if (salaryValue < 0) {
                return ResponseEntity.badRequest().body("Salary cannot be negative");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid salary format");
        }

        try {
            int ageValue = Integer.parseInt(age.toString());
            if (ageValue < 18 || ageValue > 100) {
                return ResponseEntity.badRequest().body("Age must be between 18 and 100");
            }
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid age format");
        }

        return ResponseEntity.ok("Valid input");
    }

    private boolean isValidId(String id) {
        return id != null && !id.trim().isEmpty() && id.matches("\\d+");
    }
}