package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.repositories.BeerRepository;
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIT {

    @Autowired
    BeerController beerController;
    @Autowired
    BeerRepository beerRepository;
    @Autowired
    BeerMapper beerMapper;

    @Test
    void getAllBeers() {
        List<BeerDTO> beers = beerController.getAllBeers();
        assertEquals(3, beers.size());
    }

    @Rollback
    @Transactional
    @Test
    void getAllBeersEmpty() {
        beerRepository.deleteAll();
        List<BeerDTO> beers = beerController.getAllBeers();
        assertEquals(0, beers.size());
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());
        assertNotNull(beerDTO);
    }

    @Test
    void getBeerByNonExistentId() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void createBeer() {
        ResponseEntity<@NonNull BeerDTO> beerDTO =
                beerController.createBeer(BeerDTO.builder().beerName("Skopsko").build());
        assertEquals(HttpStatus.CREATED, beerDTO.getStatusCode());
        assertNotNull(beerDTO.getBody());
        assertNotNull(beerDTO.getHeaders().getLocation());
        assertEquals("Skopsko", beerDTO.getBody().getBeerName(), "Beer name should match");

        String[] location = beerDTO.getHeaders().getLocation().getPath().split("/");
        UUID id = UUID.fromString(location[location.length - 1]);
        Beer savedBeer = beerRepository.findById(id).orElseThrow();
        assertEquals("Skopsko", savedBeer.getBeerName(), "Beer name should match");
    }

    @Test
    void updateBeerById() {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO beerDTO = beerMapper.beerToDTO(beer);

        String updatedBeerName = "Updated Beer";

        beerDTO.setBeerName(updatedBeerName);
        beerDTO.setId(null);
        beerDTO.setVersion(null);

        ResponseEntity<@NonNull BeerDTO> updatedBeer = beerController.updateBeerById(beer.getId(), beerDTO);
        assertEquals(HttpStatus.OK, updatedBeer.getStatusCode());
        assertNotNull(updatedBeer.getBody());
        assertEquals(updatedBeerName, updatedBeer.getBody().getBeerName(), "Beer name should match");
    }

    @Test
    void updateBeerByIdNotFound() {
        BeerDTO beerDTO = BeerDTO.builder().beerName("Nonexistent Beer").build();
        ResponseEntity<@NonNull BeerDTO> updatedBeer = beerController.updateBeerById(UUID.randomUUID(), beerDTO);
        assertEquals(HttpStatus.NOT_FOUND, updatedBeer.getStatusCode());
    }

    @Test
    void patchBeerById() {
    }

    @Test
    void deleteBeerById() {
    }
}
