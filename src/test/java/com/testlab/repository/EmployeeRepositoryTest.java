package com.testlab.repository;

import com.testlab.model.Employee;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    //JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        //given - precondition or setup
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();

        //when - action or behaviour that we are going to test
        Employee savedEmployee = employeeRepository.save(employee);

        //then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }


    //JUnit test for get all the employees operation
    @DisplayName("JUnit test for get all the employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenEmployeesList() {
        //given - precondition or setup
        Employee employee1 = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee employee2 = Employee.builder().firstName("Manoj").lastName("B").email("manoj@gmail.com").build();

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);

        //when - action or behaviour that we are going to test
        List<Employee> allEmployees = employeeRepository.findAll();

        //then - verify the output
        assertThat(allEmployees).isNotNull();
        assertThat(allEmployees.size()).isEqualTo(2);
    }

    //JUnit test for get employee by id
    @DisplayName("JUnit test for get the employees by id operation")
    @Test
    public void givenEmployeeObject_whenGById_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
        Employee emp = employeeRepository.findById(savedEmp.getId()).get();

        //then
        assertThat(emp).isNotNull();
        assertThat(emp.getId()).isEqualTo(savedEmp.getId());
        assertThat(emp.getFirstName()).isEqualTo("Santosh");

    }

    //JUnit test for get employee by email
    @DisplayName("JUnit test for get the employees by email operation")
    @Test
    public void givenEmployeeObject_whenGByEmail_thenReturnEmployee() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
        Employee emp = employeeRepository.findByEmail(savedEmp.getEmail()).get();

        //then
        assertThat(emp).isNotNull();
        assertThat(emp.getEmail()).isEqualTo(savedEmp.getEmail());
        assertThat(emp.getEmail()).isEqualTo("sant@gmail.com");

    }

    //JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
        Employee emp = employeeRepository.findById(savedEmp.getId()).get();
        emp.setFirstName("Manoj");
        emp.setEmail("manoj@gmail.com");
        Employee updatedEmp = employeeRepository.save(emp);

        //then
        assertThat(emp).isNotNull();
        assertThat(updatedEmp.getFirstName()).isEqualTo("Manoj");
        assertThat(savedEmp.getId()).isEqualTo(updatedEmp.getId());

    }

    //JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
//        employeeRepository.delete(savedEmp);
        employeeRepository.deleteById(savedEmp.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(savedEmp.getId());

        //then
        assertThat(optionalEmployee).isEmpty();

    }

    //JUnit test for get employee by JPQL custom query with index
    @DisplayName("JUnit test for get the employees by JPQL custom query with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
        Employee empByJPQL = employeeRepository.findByJPQL("Santosh", "K");

        //then
        assertThat(empByJPQL).isNotNull();
        assertThat(empByJPQL.getEmail()).isEqualTo(savedEmp.getEmail());
        assertThat(empByJPQL.getFirstName()).isEqualTo(savedEmp.getFirstName());

    }


    //JUnit test for get employee by JPQL custom query with named params
    @DisplayName("JUnit test for get the employees by JPQL custom query with named parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
        Employee empByJPQL = employeeRepository.findByJPQLNamedParams("Santosh", "K");

        //then
        assertThat(empByJPQL).isNotNull();
        assertThat(empByJPQL.getEmail()).isEqualTo(savedEmp.getEmail());
        assertThat(empByJPQL.getFirstName()).isEqualTo(savedEmp.getFirstName());

    }

    //JUnit test for get employee by SQL native query with index params
    @DisplayName("JUnit test for get the employees by SQL native query with index parameters")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        //given
        Employee employee = Employee.builder().firstName("Santosh").lastName("K").email("sant@gmail.com").build();
        Employee savedEmp = employeeRepository.save(employee);

        //when
        Employee empByNativeSQL = employeeRepository.findByNativeSQL("Santosh", "K");

        //then
        assertThat(empByNativeSQL).isNotNull();
        assertThat(empByNativeSQL.getEmail()).isEqualTo(savedEmp.getEmail());
        assertThat(empByNativeSQL.getFirstName()).isEqualTo(savedEmp.getFirstName());

    }

}
