package com.udacity.jdnd.course3.critter.pet;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.udacity.jdnd.course3.critter.user.Customer;
import com.udacity.jdnd.course3.critter.user.CustomerService;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService petService;
    
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {


    Pet pet = convertPetDTOToPet(petDTO);

    // Find the owner if the owner ID is set in the DTO
    if (petDTO.getOwnerId() != 0) {
        Customer owner = customerService.findCustomerById(petDTO.getOwnerId());
               
        
        // Set the pet's owner
        pet.setOwner(owner);
        
        // Save the pet first to generate and assign its ID
        Pet savedPet = petService.savePet(pet);
        
        // Ensure bidirectional relationship
        owner.getPets().add(savedPet);
        
        // Save the owner with the updated pets list
        customerService.saveCustomer(owner);
        
        return convertPetToPetDTO(savedPet);
    } else {
        // Save pet without an owner
        Pet savedPet = petService.savePet(pet);
        return convertPetToPetDTO(savedPet);
    }
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.getPetById(petId);
        if (pet != null) {
            return convertPetToPetDTO(pet);
        }
       throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
    }

    @GetMapping("/getAllPets")
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.getAllPets();
        return pets.stream()
                   .map(this::convertPetToPetDTO)
                   .collect(Collectors.toList());

    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        List<Pet> pets = petService.getPetsByOwnerId(ownerId);
        if (pets.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No pets found for this owner");
        }
        return pets.stream()
                   .map(this::convertPetToPetDTO)
                   .collect(Collectors.toList());
    }

    private Pet convertPetDTOToPet(PetDTO petDTO) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setName(petDTO.getName());
        pet.setBirthDate(petDTO.getBirthDate());
        pet.setType(petDTO.getType());


        Customer owner = new Customer();
        owner.setId(petDTO.getOwnerId()); 
        pet.setOwner(owner);
        
        return pet;
    }

    private PetDTO convertPetToPetDTO(Pet pet){
        PetDTO petDTO = new PetDTO();
        BeanUtils.copyProperties(pet, petDTO);
        if (pet.getOwner() != null) {
            petDTO.setOwnerId(pet.getOwner().getId());
        }
        return petDTO;
    }


}


