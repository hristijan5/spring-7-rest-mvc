package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Customer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    Map<UUID, Customer> customers = new HashMap<>();

    public CustomerServiceImpl() {
        UUID uuid = UUID.randomUUID();
        customers.put(uuid, Customer.builder()
                .id(uuid)
                .customerName("username1")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(1).build());
        uuid = UUID.randomUUID();
        customers.put(uuid, Customer.builder()
                .id(uuid)
                .customerName("username2")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(2).build());
    }

    @Override
    public Customer getCustomerById(UUID id) {
        return customers.get(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customers.values().stream().toList();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder().id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .version(customer.getVersion()).build();
        customers.put(savedCustomer.getId(), savedCustomer);
        return savedCustomer;
    }

    @Override
    public Customer updateCustomerById(UUID id, Customer customer) {
        Customer customerToUpdate = customers.get(id);
        customerToUpdate.setCustomerName(customer.getCustomerName());
        customerToUpdate.setVersion(customer.getVersion());
        customerToUpdate.setLastModifiedDate(LocalDateTime.now());
        return customerToUpdate;
    }

    @Override
    public Customer patchCustomerById(UUID id, Customer customer) {
        Customer customerToPatch = customers.get(id);
        if (StringUtils.hasText(customer.getCustomerName())) customerToPatch.setCustomerName(customer.getCustomerName());
        if (customer.getVersion() != null) customerToPatch.setVersion(customer.getVersion());
        return customerToPatch;
    }

    @Override
    public void deleteCustomerById(UUID id) {
        customers.remove(id);
    }

}
