package guru.springframework.spring7restmvc.services;

import guru.springframework.spring7restmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, CustomerDTO> customers = new HashMap<>();

    public CustomerServiceImpl() {
        UUID uuid = UUID.randomUUID();
        customers.put(uuid, CustomerDTO.builder()
                .id(uuid)
                .customerName("username1")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(1).build());
        uuid = UUID.randomUUID();
        customers.put(uuid, CustomerDTO.builder()
                .id(uuid)
                .customerName("username2")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(2).build());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.of(customers.get(id));
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customers.values().stream().toList();
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder().id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(customer.getVersion()).build();
        customers.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public CustomerDTO updateCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO customerToUpdate = customers.get(id);
        customerToUpdate.setCustomerName(customer.getCustomerName());
        customerToUpdate.setVersion(customer.getVersion());
        customerToUpdate.setLastModifiedDate(LocalDateTime.now());
        return customerToUpdate;
    }

    @Override
    public CustomerDTO patchCustomerById(UUID id, CustomerDTO customer) {
        CustomerDTO customerToPatch = customers.get(id);
        if (StringUtils.hasText(customer.getCustomerName())) customerToPatch.setCustomerName(customer.getCustomerName());
        if (customer.getVersion() != null) customerToPatch.setVersion(customer.getVersion());
        return customerToPatch;
    }

    @Override
    public void deleteCustomerById(UUID id) {
        customers.remove(id);
    }

}
