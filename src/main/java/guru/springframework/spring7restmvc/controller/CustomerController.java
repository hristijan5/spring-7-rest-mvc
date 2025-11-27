package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.services.CustomerService;
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
    public List<CustomerDTO> getAllCustomers() {
        log.info("Getting all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public CustomerDTO getCustomerById(@PathVariable UUID id) {
        return customerService.getCustomerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<@NonNull CustomerDTO> createCustomer(@RequestBody CustomerDTO customer) {
        CustomerDTO savedCustomer = customerService.createCustomer(customer);
        URI location = URI.create(CUSTOMER_RESOURCE_PATH + "/" + savedCustomer.getId());
        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping("{id}")
    public ResponseEntity<@NonNull CustomerDTO> updateCustomerById(@PathVariable UUID id, @RequestBody CustomerDTO customer) {
        CustomerDTO updatedCustomer = customerService.updateCustomerById(id, customer);
        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<@NonNull CustomerDTO> patchCustomerById(@PathVariable UUID id, @RequestBody CustomerDTO customer) {
        CustomerDTO patchedCustomer = customerService.patchCustomerById(id, customer);
        return new ResponseEntity<>(patchedCustomer, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteCustomerById(@PathVariable UUID id) {
        customerService.deleteCustomerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
