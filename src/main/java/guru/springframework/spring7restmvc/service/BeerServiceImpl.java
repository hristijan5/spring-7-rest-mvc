package guru.springframework.spring7restmvc.service;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    Map<UUID, Beer> beers = new HashMap<>();

    public BeerServiceImpl() {
        UUID uuid = UUID.randomUUID();
        beers.put(uuid, Beer.builder()
                .id(uuid)
                .upc("123")
                .beerName("Heineken")
                .beerStyle(BeerStyle.LAGER)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(1)
                .quantityOnHand(80)
                .price(BigDecimal.valueOf(0.99))
                .build()
        );
        uuid = UUID.randomUUID();
        beers.put(uuid, Beer.builder()
                .id(uuid)
                .upc("456")
                .beerName("Franziskaner")
                .beerStyle(BeerStyle.WHEAT)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(1)
                .quantityOnHand(100)
                .price(BigDecimal.valueOf(1.49))
                .build()
        );
        uuid = UUID.randomUUID();
        beers.put(uuid, Beer.builder()
                .id(uuid)
                .upc("789")
                .beerName("Guinness")
                .beerStyle(BeerStyle.STOUT)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .version(1)
                .quantityOnHand(50)
                .price(BigDecimal.valueOf(1.99))
                .build()
        );
    }

    @Override
    public Beer getBeerById(UUID uuid) {
        log.info("Getting beer with id {}", uuid);
        return beers.get(uuid);
    }

    @Override
    public List<Beer> getAllBeers() {
        return beers.values().stream().toList();
    }

    @Override
    public Beer createBeer(Beer beer) {
        Beer savedBeer = Beer.builder().id(UUID.randomUUID())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .upc(beer.getUpc())
                .version(beer.getVersion())
                .updateDate(LocalDateTime.now())
                .createdDate(LocalDateTime.now())
                .beerStyle(beer.getBeerStyle())
                .beerName(beer.getBeerName()).build();
        beers.put(UUID.randomUUID(), savedBeer);
        return savedBeer;
    }

    @Override
    public Beer updateBeerById(UUID id, Beer beer) {
        Beer beerToUpdate = beers.get(id);
        beerToUpdate.setBeerName(beer.getBeerName());
        beerToUpdate.setBeerStyle(beer.getBeerStyle());
        beerToUpdate.setPrice(beer.getPrice());
        beerToUpdate.setQuantityOnHand(beer.getQuantityOnHand());
        beerToUpdate.setUpdateDate(LocalDateTime.now());
        beerToUpdate.setVersion(beer.getVersion());
        beers.put(id, beerToUpdate);
        return beerToUpdate;
    }

    @Override
    public Beer patchBeerById(UUID id, Beer beer) {
        Beer beerToPatch = beers.get(id);
        if (StringUtils.hasText(beer.getBeerName())) beerToPatch.setBeerName(beer.getBeerName());
        if (StringUtils.hasText(beer.getUpc())) beerToPatch.setUpc(beer.getUpc());
        if (beer.getPrice() != null) beerToPatch.setPrice(beer.getPrice());
        if (beer.getVersion() != null) beerToPatch.setVersion(beer.getVersion());
        if (beer.getBeerStyle() != null) beerToPatch.setBeerStyle(beer.getBeerStyle());
        if (beer.getQuantityOnHand() != null) beerToPatch.setQuantityOnHand(beer.getQuantityOnHand());
        beers.put(id, beerToPatch);
        return beerToPatch;
    }

    @Override
    public void deleteBeerById(UUID id) {
        beers.remove(id);
    }

}
