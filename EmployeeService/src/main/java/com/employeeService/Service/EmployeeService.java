package com.employeeService.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employeeService.Entity.Employee;
import com.employeeService.Entity.EmployeeTaxInfo;
import com.employeeService.Repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Method to store employee details
    public void storeEmployeeDetails(Employee employee) {
        employeeRepository.save(employee);
    }

    // Method to calculate tax deductions for the current financial year
    public List<EmployeeTaxInfo> calculateTaxDeduction() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeTaxInfo> taxInfoList = new ArrayList<>();

        for (Employee employee : employees) {
            // Calculate tax for each employee and add to taxInfoList
            EmployeeTaxInfo taxInfo = calculateTaxForEmployee(employee);
            taxInfoList.add(taxInfo);
        }

        return taxInfoList;
    }

    // Method to calculate tax for a single employee
    private EmployeeTaxInfo calculateTaxForEmployee(Employee employee) {
        double yearlySalary = calculateYearlySalary(employee);
        double taxAmount = calculateTaxAmount(yearlySalary);
        double cessAmount = calculateCessAmount(yearlySalary);

        EmployeeTaxInfo taxInfo = new EmployeeTaxInfo(null, null, null, cessAmount, cessAmount, cessAmount);
        taxInfo.setEmployeeCode(employee.getEmployeeId());
        taxInfo.setFirstName(employee.getFirstName());
        taxInfo.setLastName(employee.getLastName());
        taxInfo.setYearlySalary(yearlySalary);
        taxInfo.setTaxAmount(taxAmount);
        taxInfo.setCessAmount(cessAmount);

        return taxInfo;
    }

    // Method to calculate yearly salary considering DOJ
    private double calculateYearlySalary(Employee employee) {
        Calendar dojCalendar = Calendar.getInstance();
        dojCalendar.setTime(employee.getDoj());

        Calendar currentCalendar = Calendar.getInstance();

        int monthsWorked = (currentCalendar.get(Calendar.YEAR) - dojCalendar.get(Calendar.YEAR)) * 12 +
                           (currentCalendar.get(Calendar.MONTH) - dojCalendar.get(Calendar.MONTH));

        double totalSalary = employee.getSalary() * monthsWorked;

        return totalSalary;
    }

    // Method to calculate tax amount based on yearly salary
    private double calculateTaxAmount(double yearlySalary) {
        if (yearlySalary <= 250000) {
            return 0;
        } else if (yearlySalary <= 500000) {
            return (yearlySalary - 250000) * 0.05;
        } else if (yearlySalary <= 1000000) {
            return 25000 + (yearlySalary - 500000) * 0.10;
        } else {
            return 125000 + (yearlySalary - 1000000) * 0.20;
        }
    }

    // Method to calculate cess amount based on yearly salary
    private double calculateCessAmount(double yearlySalary) {
        if (yearlySalary > 2500000) {
            return (yearlySalary - 2500000) * 0.02;
        } else {
            return 0;
        }
    }
}