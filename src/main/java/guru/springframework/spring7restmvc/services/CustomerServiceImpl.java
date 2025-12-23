package guru.springframework.spring7restmvc.services;

import guru.springframework.spring7restmvc.controller.NotFoundException;
import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.mappers.CustomerMapper;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(customerMapper.customerToCustomerDTO(customerRepository.findById(id).orElse(null)));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).toList();
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customerDto) {
        Customer customer = customerRepository.save(customerMapper.customerDTOToCustomer(customerDto));
        return customerMapper.customerToCustomerDTO(customer);
    }

    @Override
    public CustomerDTO updateCustomerById(UUID id, CustomerDTO customerDto) {
        Optional<Customer> customerToUpdate = customerRepository.findById(id);
        if (customerToUpdate.isEmpty()) throw new NotFoundException("Customer with id " + id + " not found");
        return customerMapper.customerToCustomerDTO(customerRepository.save(customerMapper.customerDTOToCustomer(customerDto)));
    }

    @Override
    public CustomerDTO patchCustomerById(UUID id, CustomerDTO customerDto) {
        Optional<Customer> customerToPatch = customerRepository.findById(id);
        if (customerToPatch.isEmpty()) throw new NotFoundException("Customer with id " + id + " not found");
        Customer customer = customerToPatch.get();
        if (StringUtils.hasText(customerDto.getCustomerName())) customer.setCustomerName(customerDto.getCustomerName());
        customer.setLastModifiedDate(LocalDateTime.now());
        return customerMapper.customerToCustomerDTO(customerRepository.save(customer));
    }

    @Override
    public void deleteCustomerById(UUID id) {
        customerRepository.deleteById(id);
    }

}
