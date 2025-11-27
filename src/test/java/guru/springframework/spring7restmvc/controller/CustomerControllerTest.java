package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.CustomerDTO;
import guru.springframework.spring7restmvc.services.CustomerService;
import guru.springframework.spring7restmvc.services.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    public static final String CUSTOMER_RESOURCE_PATH = "/api/v1/customer";
    private static final String CUSTOMER_RESOURCE_PATH_WITH_ID = CUSTOMER_RESOURCE_PATH + "/{customerId}";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper mapper;

    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void getAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        mockMvc.perform(get(CUSTOMER_RESOURCE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDTO customer = customerServiceImpl.getAllCustomers().getFirst();
        given(customerService.getCustomerById(customer.getId())).willReturn(Optional.of(customer));

        mockMvc.perform(get(CUSTOMER_RESOURCE_PATH_WITH_ID, customer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(customer.getId().toString()))
                .andExpect(jsonPath("$.customerName").value(customer.getCustomerName()));
    }

    @Test
    void getCustomerIdNotFound() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(CUSTOMER_RESOURCE_PATH_WITH_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createCustomer() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.getAllCustomers().getFirst();
        testCustomer.setId(null);
        testCustomer.setVersion(null);

        given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(customerServiceImpl.getAllCustomers().getFirst());

        mockMvc.perform(post(CUSTOMER_RESOURCE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateCustomerById() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.getAllCustomers().getFirst();
        testCustomer.setCustomerName("Updated CustomerDTO Name");

        given(customerService.updateCustomerById(any(UUID.class), any(CustomerDTO.class))).willReturn(testCustomer);

        mockMvc.perform(put(CUSTOMER_RESOURCE_PATH_WITH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testCustomer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Updated CustomerDTO Name"));
    }

    @Test
    void patchCustomerById() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.getAllCustomers().getFirst();

        HashMap<String, String> customerPatch = new HashMap<>();
        customerPatch.put("customerName", "Updated CustomerDTO Name");

        mockMvc.perform(patch(CUSTOMER_RESOURCE_PATH_WITH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerPatch)))
                .andExpect(status().isOk());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<CustomerDTO> customerArgumentCaptor = ArgumentCaptor.forClass(CustomerDTO.class);

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());

        assertEquals(testCustomer.getId(), uuidArgumentCaptor.getValue());
        assertEquals("Updated CustomerDTO Name", customerArgumentCaptor.getValue().getCustomerName());
    }

    @Test
    void deleteCustomerById() throws Exception {
        CustomerDTO testCustomer = customerServiceImpl.getAllCustomers().getFirst();

        mockMvc.perform(delete(CUSTOMER_RESOURCE_PATH_WITH_ID, testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());

        assertEquals(testCustomer.getId(), uuidArgumentCaptor.getValue());
    }

}
