package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
     @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void setAvailability(Long employeeId, Set<DayOfWeek> daysAvailable) {
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
        employee.setDaysAvailable(daysAvailable);
        employeeRepository.save(employee);
    }

    public List<Employee> findEmployeesForService(EmployeeRequestDTO requestDTO) {
        LocalDate requestedDate = requestDTO.getDate();
        Set<EmployeeSkill> requiredSkills = requestDTO.getSkills();

        // Find employees available on the requested day and with the required skills
        return employeeRepository.findAll().stream()
                .filter(employee -> employee.getDaysAvailable().contains(requestedDate.getDayOfWeek()))
                .filter(employee -> employee.getSkills().containsAll(requiredSkills))
                .collect(Collectors.toList());
    }

    public Employee getEmployeeById (Long employeeId){
        return employeeRepository.findById(employeeId)
        .orElseThrow(() -> new IllegalArgumentException("Employee not found with ID: " + employeeId));
    }
}
