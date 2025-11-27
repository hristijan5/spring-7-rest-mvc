package guru.springframework.spring7restmvc.repositories;

import guru.springframework.spring7restmvc.entities.Customer;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<@NonNull Customer, @NonNull UUID> {

}
