package com.udacity.jdnd.course3.critter.user;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udacity.jdnd.course3.critter.pet.Pet;
import com.udacity.jdnd.course3.critter.pet.PetService;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private PetService petService;
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        //  // NOT WORKING
        //  Customer customer = new Customer();
        //  customer.setId(customerDTO.getId());
        //  customer.setName(customerDTO.getName());
        //  customer.setPhoneNumber(customerDTO.getPhoneNumber());
        //  customer.setNotes(customerDTO.getNotes());
 
        //  if (customerDTO.getPetIds() != null) {
        //      List<Pet> pets = customerDTO.getPetIds().stream()
        //              .map(petService::getPetById) // Assuming this method exists in PetService
        //              .collect(Collectors.toList());
        //      customer.setPets(pets);
        //  }
 
        //  // Save the customer and convert back to DTO
        //  Customer savedCustomer = customerService.saveCustomer(customer);
        //  return convertCustomerToDTO(savedCustomer);


        List<Long> petIds = customerDTO.getPetIds();
        
        List<Pet> pets = new ArrayList<>();

        if (petIds != null) {
            for (Long petId : petIds) {
                pets.add(petService.getPetById(petId));
            }
        }

        Customer customer = convertCustomerDTOToCustomer(customerDTO);
        customer.setPets(pets);
        Customer savedCustomer = customerService.saveCustomer(customer);
        return convertCustomerToDTO(savedCustomer);


    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.getAllCustomers();
        return customers.stream()
                .map(this::convertCustomerToDTO)
                .collect(Collectors.toList());


        // List<Customer> customers = this.customerService.getAllCustomers();
        // List<CustomerDTO> customerDTOS = new ArrayList<>();
        // for(Customer customer : customers){
        //     customerDTOS.add(convertCustomerToDTO(customer));
        // }
        // return customerDTOS;
    }
    

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
   
        Pet pet = this.petService.getPetById(petId);
        if(pet.getOwner() != null){
            return convertCustomerToDTO(pet.getOwner());
        }
        else throw new UnsupportedOperationException("Apparently the Pet doesn't have an Owner yet!");
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertDTOToEmployee(employeeDTO);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return convertEmployeeToDTO(savedEmployee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee != null) {
            return convertEmployeeToDTO(employee);
        }
        return null;
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setAvailability(employeeId, daysAvailable);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesForService(employeeDTO);
        return employees.stream()
                .map(this::convertEmployeeToDTO)
                .collect(Collectors.toList());
    }

    private CustomerDTO convertCustomerToDTO(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);

        if (customer.getPets() != null) {
            customerDTO.setPetIds(
                customer.getPets().stream()
                        .map(Pet::getId)
                        .collect(Collectors.toList())
            );
        }

        // List<Pet> pets = customer.getPets();

        // if (pets != null) {
        //     List<Long> petIds = new ArrayList<>();

        //     for (Pet pet : pets) {
        //         petIds.add(pet.getId());
        //     }
        //     customerDTO.setPetIds(petIds);
        // }
        return customerDTO;
    }

    private Customer convertCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        // Copy simple properties from DTO to entity
        customer.setId(customerDTO.getId());
        customer.setName(customerDTO.getName());
        customer.setPhoneNumber(customerDTO.getPhoneNumber());
        customer.setNotes(customerDTO.getNotes());
    
        // Convert pet IDs from DTO to Pet entities if needed
        if (customerDTO.getPetIds() != null) {
            List<Pet> pets = customerDTO.getPetIds().stream()
                    .map(petId -> {
                        Pet pet = new Pet();
                        pet.setId(petId); // Assuming you only need the ID set here
                        return pet;
                    })
                    .collect(Collectors.toList());
            customer.setPets(pets);
        }
    
        return customer;
    }

    private Employee convertDTOToEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setSkills(employeeDTO.getSkills());
        employee.setDaysAvailable(employeeDTO.getDaysAvailable());
        return employee;
    }

    private EmployeeDTO convertEmployeeToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
    
        return employeeDTO;
    }

}
