package guru.springframework.spring7restmvc.mappers;

import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.model.CustomerDTO;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    CustomerDTO customerToCustomerDTO(Customer customer);

}
