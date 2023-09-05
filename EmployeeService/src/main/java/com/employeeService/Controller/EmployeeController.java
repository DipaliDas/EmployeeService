package com.employeeService.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.employeeService.Entity.Employee;
import com.employeeService.Entity.EmployeeTaxInfo;
import com.employeeService.Service.EmployeeService;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<String> storeEmployeeDetails( @RequestBody Employee employee) {
        employeeService.storeEmployeeDetails(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee details saved successfully");
    }

    @GetMapping("/tax-deduction")
    public ResponseEntity<List<EmployeeTaxInfo>> getTaxDeduction() {
        List<EmployeeTaxInfo> taxInfoList = employeeService.calculateTaxDeduction();
        return ResponseEntity.ok(taxInfoList);
    }
}
