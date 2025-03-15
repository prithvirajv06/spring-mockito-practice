package com.prithvi.mockitoPractice.service;

import com.prithvi.mockitoPractice.entity.Employee;
import com.prithvi.mockitoPractice.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TestService {


    @InjectMocks
    private EmployeeService employeeService = new EmployeeServiceImpl();

    @Mock
    private EmployeeRepository employeeRepository;

    public void setUp() {
        Employee employee = new Employee();
        employee.setName("Prithviraj V");

        Mockito.when(employeeRepository.findByName(employee.getName()))
                .thenReturn(employee);
    }

    @Captor
    ArgumentCaptor<String> argumentCaptor;

    @Test
    public void whenValidName_thenEmployeeShouldBeFound() {
        setUp();
        String name = "Prithviraj V";
        Employee found = employeeService.getEmployeeByName(name);

        Assertions.assertEquals(found.getName(),name);
        verify(employeeRepository).findByName(argumentCaptor.capture()); // ✅ Ensure method was called
        Assertions.assertEquals(argumentCaptor.getAllValues().get(0), "Prithviraj V");
    }
}
