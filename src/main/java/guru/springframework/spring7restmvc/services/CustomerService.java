package guru.springframework.spring7restmvc.services;

import guru.springframework.spring7restmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO createCustomer(CustomerDTO customer);

    CustomerDTO updateCustomerById(UUID id, CustomerDTO customer);

    CustomerDTO patchCustomerById(UUID id, CustomerDTO customer);

    void deleteCustomerById(UUID id);

}
