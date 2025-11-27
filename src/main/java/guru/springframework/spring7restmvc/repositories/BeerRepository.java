package guru.springframework.spring7restmvc.repositories;

import guru.springframework.spring7restmvc.entities.Beer;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<@NonNull Beer, @NonNull UUID> {

}
