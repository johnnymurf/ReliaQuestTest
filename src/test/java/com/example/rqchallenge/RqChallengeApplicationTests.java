package com.example.rqchallenge;

import com.example.rqchallenge.employees.EmployeeController;
import com.example.rqchallenge.dtos.Employee;
import com.example.rqchallenge.dtos.EmployeeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class RqChallengeApplicationTests {


    private EmployeeController employeeController;

    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        employeeController = new EmployeeController(restTemplate);
    }


    @Test
    void getAllEmployees_Success() {
        List<Employee> mockEmployees = Collections.singletonList(
                Employee.builder().id("1").employeeName("Mr Test").employeeSalary("50000").employeeAge("30").profileImage("").build()
        );
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(mockEmployees);

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getEmployeesByNameSearch_Success() {
        List<Employee> mockEmployees = Arrays.asList(
                Employee.builder().id("1").employeeName("Mr Test 1").employeeSalary("50000").employeeAge("30").profileImage("").build(),
                Employee.builder().id("2").employeeName("Mr Test 2").employeeSalary("60000").employeeAge("35").profileImage("").build()
        );
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(mockEmployees);

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch("Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getEmployeeById_Success() {
        Employee mockEmployee = Employee.builder().id("1").employeeName("Mr TestID").employeeSalary("50000").employeeAge("30").profileImage("").build();
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(Collections.singletonList(mockEmployee));

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<Employee> response = employeeController.getEmployeeById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mr TestID", response.getBody().getEmployeeName());
    }

    @Test
    void getHighestSalaryOfEmployees_Success() {
        List<Employee> mockEmployees = Arrays.asList(
                Employee.builder().id("1").employeeName("Mr Wealthy").employeeSalary("50000").employeeAge("30").profileImage("").build(),
                Employee.builder().id("2").employeeName("Mrs Wealthier").employeeSalary("60000").employeeAge("35").profileImage("").build()
        );
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(mockEmployees);

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(60000, response.getBody());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_Success() {
        List<Employee> mockEmployees = Arrays.asList(
                Employee.builder().id("1").employeeName("Mr Test 1").employeeSalary("50000").employeeAge("30").profileImage("").build(),
                Employee.builder().id("2").employeeName("Mr Test 2").employeeSalary("50001").employeeAge("35").profileImage("").build(),
                Employee.builder().id("3").employeeName("Mr Test 3").employeeSalary("55002").employeeAge("40").profileImage("").build(),
                Employee.builder().id("4").employeeName("Mr Test 4").employeeSalary("55003").employeeAge("40").profileImage("").build(),
                Employee.builder().id("5").employeeName("Mr Test 5").employeeSalary("55004").employeeAge("40").profileImage("").build(),
                Employee.builder().id("6").employeeName("Mr Test 6").employeeSalary("55005").employeeAge("40").profileImage("").build(),
                Employee.builder().id("7").employeeName("Mr Test 7").employeeSalary("55006").employeeAge("40").profileImage("").build(),
                Employee.builder().id("8").employeeName("Mr Test 8").employeeSalary("55007").employeeAge("40").profileImage("").build(),
                Employee.builder().id("9").employeeName("Mr Test 9").employeeSalary("55008").employeeAge("40").profileImage("").build(),
                Employee.builder().id("10").employeeName("Mr Test 10").employeeSalary("55009").employeeAge("40").profileImage("").build(),
                Employee.builder().id("11").employeeName("Mr Test 11").employeeSalary("5500").employeeAge("40").profileImage("").build()
        );
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(mockEmployees);

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().size());
<<<<<<< HEAD
        assertEquals("Mr Test 10", response.getBody().get(0));
=======
        assertEquals("Mr Test 1", response.getBody().get(0));
>>>>>>> 68e3a677357b57068cb80c8622a05c46c2cdb56e
        assertTrue(response.getBody().stream()
                        .noneMatch(name -> name.toLowerCase().contains("Mr test 11")),
                "No employee name should contain Mr Test 11");
    }


    @Test
    void getHighestSalaryOfEmployees_EmptyList() {
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(Collections.emptyList());

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<Integer> response = employeeController.getHighestSalaryOfEmployees();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getTopTenHighestEarningEmployeeNames_LessThanTenEmployees() {
        List<Employee> mockEmployees = Arrays.asList(
                Employee.builder().id("1").employeeName("Mr Test 1").employeeSalary("50000").employeeAge("30").profileImage("").build(),
                Employee.builder().id("2").employeeName("Mr Test 2").employeeSalary("60000").employeeAge("35").profileImage("").build()
        );
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(mockEmployees);

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<List<String>> response = employeeController.getTopTenHighestEarningEmployeeNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getEmployeeById_InvalidId() {
        ResponseEntity<Employee> response = employeeController.getEmployeeById("invalid_id");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }


    /* Create Employee tests */

    @Test
    void createEmployee_Success() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr CreateEmployee");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "30");

        Employee createdEmployee = Employee.builder().id("1").employeeName("Mrs Test").employeeSalary("50000").employeeAge("30").profileImage("").build();
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);
        mockResponse.setEmployees(Collections.singletonList(createdEmployee));

        when(restTemplate.postForEntity(anyString(), any(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Mrs Test", response.getBody().getEmployeeName());
    }

    @Test
    void createEmployee_InvalidSalary() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr Invalid");
        employeeInput.put("salary", "invalid_salary");
        employeeInput.put("age", "30");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_NegativeSalary() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr Negative");
        employeeInput.put("salary", "-50000");
        employeeInput.put("age", "30");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_InvalidAge() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr Timeless");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "invalid_age");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_NegativeAge() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr How");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "-30");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_AgeExceedsMaximum() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr CleanLife");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "150");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_EmptyName() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "30");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_NullName() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", null);
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "30");

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }



    @Test
    void getAllEmployees_NetworkError() {
        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenThrow(new RestClientException("Network Error"));

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getEmployeeById_NotFound() {
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.NOT_FOUND);
        mockResponse.setEmployees(Collections.emptyList());

        when(restTemplate.getForEntity(anyString(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.NOT_FOUND));

        ResponseEntity<Employee> response = employeeController.getEmployeeById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void createEmployee_ServerError() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "Mr Test");
        employeeInput.put("salary", "50000");
        employeeInput.put("age", "30");

        when(restTemplate.postForEntity(anyString(), any(), eq(EmployeeResponse.class)))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNull(response.getBody());
    }


    /*Delete Employee by ID tests*/

    @Test
    void deleteEmployeeById_Success() {
        EmployeeResponse mockResponse = new EmployeeResponse();
        mockResponse.setStatus(HttpStatus.OK);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(EmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        ResponseEntity<String> response = employeeController.deleteEmployeeById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Successfully deleted employee with id: 1"));
    }


    @Test
    void deleteEmployeeById_NotFound() {
        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(), eq(EmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = employeeController.deleteEmployeeById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteEmployeeById_InvalidId() {
        ResponseEntity<String> response = employeeController.deleteEmployeeById("invalid_id");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid employee ID"));
    }


}