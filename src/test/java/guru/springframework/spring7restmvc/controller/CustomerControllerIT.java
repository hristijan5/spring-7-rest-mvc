package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;
    @Autowired
    CustomerRepository customerRepository;

    @Test
    void getAllCustomers() {
        List<CustomerDTO> customers = customerController.getAllCustomers();
        assertEquals(2, customers.size());
    }

    @Rollback
    @Transactional
    @Test
    void getAllCustomersEmpty() {
        customerRepository.deleteAll();
        List<CustomerDTO> customers = customerController.getAllCustomers();
        assertEquals(0, customers.size());
    }

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO customerDTO = customerController.getCustomerById(customer.getId());
        assertEquals(customer.getCustomerName(), customerDTO.getCustomerName());
    }

    @Test
    void getCustomerByNonExistentId() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Test
    void createCustomer() {
    }

    @Test
    void updateCustomerById() {
    }

    @Test
    void patchCustomerById() {
    }

    @Test
    void deleteCustomerById() {
    }
}
