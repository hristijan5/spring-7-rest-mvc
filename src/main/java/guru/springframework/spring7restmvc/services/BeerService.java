package guru.springframework.spring7restmvc.services;

import guru.springframework.spring7restmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {

    Optional<BeerDTO> getBeerById(UUID uuid);

    List<BeerDTO> getAllBeers();

    BeerDTO createBeer(BeerDTO build);

    Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beer);

    BeerDTO patchBeerById(UUID id, BeerDTO beer);

    void deleteBeerById(UUID id);

}
