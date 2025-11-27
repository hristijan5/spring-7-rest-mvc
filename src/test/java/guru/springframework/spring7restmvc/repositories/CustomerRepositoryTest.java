package guru.springframework.spring7restmvc.repositories;

import guru.springframework.spring7restmvc.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveCustomer() {
        Customer savedCustomer = customerRepository.save(Customer.builder().customerName("Test Customer").build());

        assertNotNull(savedCustomer);
        assertNotNull(savedCustomer.getId());
        assertEquals("Test Customer", savedCustomer.getCustomerName(),
                "Customer name should be Test Customer");
    }

}
