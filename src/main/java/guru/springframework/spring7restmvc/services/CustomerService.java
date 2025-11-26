package guru.springframework.spring7restmvc.services;

import guru.springframework.spring7restmvc.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(UUID id);

    Customer createCustomer(Customer customer);

    Customer updateCustomerById(UUID id, Customer customer);

    Customer patchCustomerById(UUID id, Customer customer);

    void deleteCustomerById(UUID id);

}
