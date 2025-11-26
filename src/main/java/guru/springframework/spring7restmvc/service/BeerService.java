package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    Beer getBeerById(UUID uuid);

    List<Beer> getAllBeers();

    Beer createBeer(Beer build);

    Beer updateBeerById(UUID id, Beer beer);

    Beer patchBeerById(UUID id, Beer beer);

    void deleteBeerById(UUID id);

}
