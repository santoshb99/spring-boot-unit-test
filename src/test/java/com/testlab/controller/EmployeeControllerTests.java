package com.testlab.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testlab.model.Employee;
import com.testlab.service.EmployeeService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("JUnit test for Create Employee Rest Api")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when - action or behaviour that we are going to test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then - verify the result or output using assert statements
        response.andDo(MockMvcResultHandlers.print()).
                andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @DisplayName("JUnit test for Get All Employees Rest Api")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given
        List<Employee> listOfEmps = new ArrayList<>();
        Employee employee1 = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Manoj").lastName("K").email("manoj@gmail.com").build();

        listOfEmps.add(employee1);
        listOfEmps.add(employee2);

        BDDMockito.given(employeeService.getAllEmployees()).willReturn(listOfEmps);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(listOfEmps.size())));
    }

    //Positive scenario - valid employee id
    @DisplayName("JUnit test for GET employee by id Rest Api")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given
        long empId = 1L;
        Employee employee = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.of(employee));

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", empId));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    //Negative scenario - valid employee id
    @DisplayName("JUnit test for GET employee by id Rest Api")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given
        long empId = 1L;
        Employee employee = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.empty());

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", empId));

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    //JUnit test for update employee REST API - Positive scenario
    @DisplayName("JUnit test for update employee Rest Api")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given
        long empId = 1L;
        Employee savedEmp = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

        Employee updatedEmp = Employee.builder().firstName("Sonu").lastName("kumar").email("sonu@gmail.com").build();

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.of(savedEmp));
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmp.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmp.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmp.getEmail())));
    }

    //JUnit test for update employee REST API - Negative scenario
    @DisplayName("JUnit test for update employee Rest Api")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given
        long empId = 1L;
        Employee savedEmp = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

        Employee updatedEmp = Employee.builder().firstName("Sonu").lastName("kumar").email("sonu@gmail.com").build();

        BDDMockito.given(employeeService.getEmployeeById(empId)).willReturn(Optional.empty());
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("JUnit test for delete Employee Rest Api")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn204() throws Exception {
        //given
        long empId = 1L;

        BDDMockito.willDoNothing().given(employeeService).deleteEmployee(empId);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", empId));

        //then
        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }
}
