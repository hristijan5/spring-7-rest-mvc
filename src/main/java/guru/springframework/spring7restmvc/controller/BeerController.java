package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.services.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    private final BeerService beerService;

    @GetMapping
    public List<BeerDTO> getAllBeers() {
        log.info("Getting all beers");
        return beerService.getAllBeers();
    }

    @GetMapping("{id}")
    public BeerDTO getBeerById(@PathVariable UUID id) {
        log.info("Getting beer with id {}", id);
        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping
    public ResponseEntity<@NonNull BeerDTO> createBeer(@RequestBody BeerDTO beer) {
        BeerDTO savedBeer = beerService.createBeer(beer);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(URI.create("/api/v1/beer/" + savedBeer.getId()));
        return new ResponseEntity<>(savedBeer, httpHeaders, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<@NonNull BeerDTO> updateBeerById(@PathVariable UUID id, @RequestBody BeerDTO beer) {
        BeerDTO updatedBeer = beerService.updateBeerById(id, beer);
        return new ResponseEntity<>(updatedBeer, HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<@NonNull BeerDTO> patchBeerById(@PathVariable UUID id, @RequestBody BeerDTO beer) {
        BeerDTO patchedBeer = beerService.patchBeerById(id, beer);
        return new ResponseEntity<>(patchedBeer, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteBeerById(@PathVariable UUID id) {
        beerService.deleteBeerById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
