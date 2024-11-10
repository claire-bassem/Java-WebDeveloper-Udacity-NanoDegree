package com.udacity.jdnd.course3.critter.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.Employee;
import com.udacity.jdnd.course3.critter.user.EmployeeRepository;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {


@Autowired
private  ScheduleService scheduleService;
    @Autowired
private EmployeeRepository employeeRepository;

@Autowired
private PetRepository petRepository;
    @PostMapping("/create")
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) { 
        Schedule schedule = convertDTOToSchedule(scheduleDTO); 
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        return convertScheduleToDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.getAllSchedules();
    return schedules.stream()
            .map(this::convertScheduleToDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules = scheduleService.getScheduleForPet(petId);
        return schedules.stream()
                .map(this::convertScheduleToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules = scheduleService.getScheduleForEmployee(employeeId);
    return schedules.stream()
            .map(this::convertScheduleToDTO)
            .collect(Collectors.toList());
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules = scheduleService.getScheduleForCustomer(customerId);
        return schedules.stream()
                .map(this::convertScheduleToDTO)
                .collect(Collectors.toList());
    }



    // @GetMapping("/employee/{employeeId}")
    // public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
    //     List<Schedule> schedules = this.scheduleService.getScheduleForEmployee(employeeId);
    //     List<ScheduleDTO> scheduleDTOS = new ArrayList<>();
    //     for(Schedule schedule:schedules){
    //         scheduleDTOS.add(convertScheduleToDTO(schedule));
    //     }
    //     return scheduleDTOS;
    // }


 private ScheduleDTO convertScheduleToDTO(Schedule schedule) {

        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        // setting the rest (pets, employees) of properties for schedulePTO
        scheduleDTO.setActivities(schedule.getActivities());

        List<Pet> pets = schedule.getPets();
        List<Long> petId = new ArrayList<>();
        for (Pet pet : pets) {
            petId.add(pet.getId());
        }
        scheduleDTO.setPetIds(petId);
        List<Employee> employees = schedule.getEmployees();
        List<Long> employeeId = new ArrayList<>();
        for (Employee employee : employees) {
            employeeId.add(employee.getId());
        }
        scheduleDTO.setEmployeeIds(employeeId);
        return scheduleDTO;

    }




private Schedule convertDTOToSchedule(ScheduleDTO dto) {
    Schedule schedule = new Schedule();
    schedule.setDate(dto.getDate());
    schedule.setActivities(dto.getActivities());
    
    // Load and set employees based on employeeIds
    if (dto.getEmployeeIds() != null) {
        List<Employee> employees = dto.getEmployeeIds().stream()
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Employee not found for ID: " + id)))
                .collect(Collectors.toList());
        schedule.setEmployees(employees);
    }

    // Load and set pets based on petIds
    if (dto.getPetIds() != null) {
        List<Pet> pets = dto.getPetIds().stream()
                .map(id -> petRepository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("Pet not found for ID: " + id)))
                .collect(Collectors.toList());
        schedule.setPets(pets);
    }
    
    return schedule;
}


}
