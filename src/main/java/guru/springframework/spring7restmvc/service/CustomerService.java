package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    Customer getCustomerById(UUID id);

    List<Customer> getAllCustomers();

    Customer createCustomer(Customer customer);

    Customer updateCustomerById(UUID id, Customer customer);

    Customer patchCustomerById(UUID id, Customer customer);

    void deleteCustomerById(UUID id);

}
