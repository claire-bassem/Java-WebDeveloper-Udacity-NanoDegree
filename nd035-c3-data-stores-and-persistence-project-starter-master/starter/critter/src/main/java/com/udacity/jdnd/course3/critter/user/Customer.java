package com.udacity.jdnd.course3.critter.user;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.udacity.jdnd.course3.critter.pet.Pet;
@Entity
public class Customer extends User {

    private String phoneNumber;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, targetEntity = Pet.class)
    private List<Pet> pets;
    @Column(length=5000)
    private String notes;

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
