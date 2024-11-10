package com.udacity.jdnd.course3.critter.schedule;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetRepository;
import com.udacity.jdnd.course3.critter.user.CustomerRepository;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CustomerRepository customerRepository;


    public List<Schedule> getScheduleForPet(Long petId) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getPets().stream()
                        .anyMatch(pet -> pet.getId().equals(petId)))
                .collect(Collectors.toList());
    }

    public List<Schedule> getScheduleForEmployee(Long employeeId) {
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getEmployees().stream()
                        .anyMatch(employee -> employee.getId().equals(employeeId)))
                .collect(Collectors.toList());
    }

    public List<Schedule> getScheduleForCustomer(Long customerId) {
        // Find all pets owned by the customer
        List<Pet> customerPets = petRepository.findByOwnerId(customerId);

        // Return schedules for all pets owned by the customer
        return scheduleRepository.findAll().stream()
                .filter(schedule -> schedule.getPets().stream()
                        .anyMatch(customerPets::contains))
                .collect(Collectors.toList());
    }


      public Schedule saveSchedule(Schedule schedule){
        return this.scheduleRepository.save(schedule);
    }

    public List<Schedule> getAllSchedules(){
        return this.scheduleRepository.findAll();
    }

    // public List<Schedule> getScheduleForPet(Long petId){

    //     return this.scheduleRepository.getScheduleByPets_Id(petId);
    // }

    
    // public List<Schedule> getScheduleForCustomer(Long customerId) {
    //     Optional<Customer> optionalOwner = this.customerRepository.findById(customerId);

    //     if (!optionalOwner.isPresent()) {
    //         throw new NoSuchElementException("Owner not found for the given ID.");
    //     }
    //     else{
    //         Customer customer = optionalOwner.get();
    //         List<Pet> pets = customer.getPets();
    //         List<Schedule> schedules = new ArrayList<>();

    //         for (Pet pet : pets) {
    //             schedules.addAll(scheduleRepository.getScheduleByPets_Id(pet.getId()));
    //         }
    //         return schedules;
    //     }
    // }

    // public List<Schedule> getScheduleForEmployee(Long employeeId) {
    //     return scheduleRepository.getScheduleByEmployees_Id(employeeId);
    // }
    
}
