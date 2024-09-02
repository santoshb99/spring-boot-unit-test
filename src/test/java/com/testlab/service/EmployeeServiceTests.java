package com.testlab.service;


import com.testlab.exception.ResourceNotFoundException;
import com.testlab.model.Employee;
import com.testlab.repository.EmployeeRepository;
import com.testlab.service.impl.EmployeeServiceImpl;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

@ExtendWith(MockitoExtension.class)  // If we are using @Mock and @InjectMocks that supports it
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    public void setUp(){
        // Instead we can use annotations -> Mock and InjectMocks
       // employeeRepository = Mockito.mock(EmployeeRepository.class);
      //  employeeService = new EmployeeServiceImpl(employeeRepository);

    }

    //JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject(){
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();

        //stubbing the methods present in the service method
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getFirstName()).isEqualTo("Santosh");
    }

    //JUnit test for saveEmployee method which throws exception
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnException(){
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();

        //stubbing the methods present in the service method
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));
//        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when
        Assertions.assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));

        //then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnEmployeesList(){
        //given
        List<Employee> listEmps = new ArrayList<>();
        Employee employee1 = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Manoj").lastName("K").email("manoj@gmail.com").build();

        listEmps.add(employee1);
        listEmps.add(employee2);

        BDDMockito.given(employeeRepository.findAll()).willReturn(listEmps);

        //when
        List<Employee> emps = employeeService.getAllEmployees();

        //then
        assertThat(emps).isNotNull();
        assertThat(emps.size()).isEqualTo(2);
    }

    // JUnit test for getAllEmployees method - (Negative scenario)
    @DisplayName("JUnit test for getAllEmployees method - (Negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList(){
        //given
        BDDMockito.given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when
        List<Employee> emps = employeeService.getAllEmployees();

        //then
        assertThat(emps).isEmpty();
        assertThat(emps.size()).isEqualTo(0);
    }

    //JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject(){

        Employee employee = Employee.builder().id(2L).firstName("Sonu").lastName("k").email("sonu@gmail.com").build();
        
        //given
        BDDMockito.given(employeeRepository.findById(2L)).willReturn(Optional.of(employee));
        
        //when
        Employee empObj = employeeService.getEmployeeById(employee.getId()).get();

        //then
        assertThat(empObj).isNotNull();
        assertThat(empObj.getId()).isEqualTo(employee.getId());
    }

    //JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee(){

        Employee employee = Employee.builder().id(2L).firstName("Sonu").lastName("k").email("sonu@gmail.com").build();

        //given
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setLastName("kumar");
        //when
        Employee empObj = employeeService.updateEmployee(employee);

        //then
        assertThat(empObj).isNotNull();
        assertThat(empObj.getLastName()).isEqualTo("kumar");
    }

    //JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnNothing(){

        Employee employee = Employee.builder().id(2L).firstName("Sonu").lastName("k").email("sonu@gmail.com").build();
        long empId = 2L;
        //given
        BDDMockito.willDoNothing().given(employeeRepository).deleteById(empId);

        //when
        employeeService.deleteEmployee(empId);

        //then
        verify(employeeRepository, times(1)).deleteById(empId);
    }
}
