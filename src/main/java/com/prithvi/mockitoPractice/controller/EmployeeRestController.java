package com.prithvi.mockitoPractice.controller;

import com.prithvi.mockitoPractice.entity.Employee;
import com.prithvi.mockitoPractice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeRestController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employee")
    public Employee getAllEmployees(@RequestParam("id") String id) {
        return employeeService.getEmployeeByName(id);
    }

}
