package guru.springframework.spring6restmvc.controller;

import guru.springframework.spring6restmvc.model.Customer;
import guru.springframework.spring6restmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private static final String CUSTOMER_RESOURCE_PATH = "/api/v1/customer";
    private final CustomerService customerService;

    @GetMapping
    public List<Customer> getAllCustomers() {
        log.info("Getting all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id);
    }

    @PostMapping
    public ResponseEntity<@NonNull Customer> createCustomer(@RequestBody Customer customer) {
        Customer savedCustomer = customerService.createCustomer(customer);
        URI location = URI.create(CUSTOMER_RESOURCE_PATH + "/" + savedCustomer.getId());
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping("{id}")
    public ResponseEntity<@NonNull Customer> updateCustomerById(@PathVariable UUID id, @RequestBody Customer customer) {
        Customer updatedCustomer = customerService.updateCustomerById(id, customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<@NonNull Customer> patchCustomerById(@PathVariable UUID id, @RequestBody Customer customer) {
        Customer patchedCustomer = customerService.patchCustomerById(id, customer);
        return new ResponseEntity<>(patchedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable UUID id) {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
