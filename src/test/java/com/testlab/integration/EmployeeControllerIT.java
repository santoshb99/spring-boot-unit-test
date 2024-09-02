package com.testlab.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testlab.model.Employee;
import com.testlab.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

// This Test is directly using mysql for the testing which is a dependency

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

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

    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        //given
        List<Employee> listOfEmps = new ArrayList<>();
        Employee employee1 = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Manoj").lastName("K").email("manoj@gmail.com").build();

        listOfEmps.add(employee1);
        listOfEmps.add(employee2);

        employeeRepository.saveAll(listOfEmps);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(listOfEmps.size())));
    }

    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        //given

        Employee employee = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();
        employeeRepository.save(employee);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employee.getId()));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())));
    }

    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception {
        //given
        long empId = 1L;
        Employee employee = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();
        employeeRepository.save(employee);

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", empId));

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() throws Exception {
        //given
        long empId = 1L;
        Employee savedEmp = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();
        employeeRepository.save(savedEmp);

        Employee updatedEmp = Employee.builder().firstName("Sonu").lastName("kumar").email("sonu@gmail.com").build();


        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", savedEmp.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(updatedEmp.getFirstName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(updatedEmp.getLastName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(updatedEmp.getEmail())));
    }

    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        //given
        long empId = 1L;
        Employee savedEmp = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();

        Employee updatedEmp = Employee.builder().firstName("Sonu").lastName("kumar").email("sonu@gmail.com").build();

        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", empId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmp)));

        //then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn204() throws Exception {
        //given
        long empId = 1L;
        Employee savedEmp = Employee.builder().firstName("Santosh").lastName("k").email("sant@gmail.com").build();
        employeeRepository.save(savedEmp);
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", savedEmp.getId()));

        //then
        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }
}
