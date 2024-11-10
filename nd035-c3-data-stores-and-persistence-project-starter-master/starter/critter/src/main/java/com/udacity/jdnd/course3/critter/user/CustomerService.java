package com.udacity.jdnd.course3.critter.user;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer saveCustomer(Customer customer) {
     //   System.out.println("Before saving customer");
        Customer savedCustomer= customerRepository.save(customer);
      //  System.out.println("Before saving customer");
        return savedCustomer;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerById(Long id){
        Optional<Customer> optionalOwner = this.customerRepository.findById(id);
        if(optionalOwner.isPresent()){
            Customer owner = optionalOwner.get();
            return owner;
        }
        return optionalOwner.orElseThrow(NoSuchElementException::new);
    }
}
