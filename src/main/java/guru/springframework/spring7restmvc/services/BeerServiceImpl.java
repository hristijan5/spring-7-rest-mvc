package guru.springframework.spring7restmvc.services;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.mappers.BeerMapper;
import guru.springframework.spring7restmvc.model.BeerDTO;
import guru.springframework.spring7restmvc.repositories.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Optional<BeerDTO> getBeerById(UUID uuid) {
        log.info("Getting beer with id {}", uuid);
        return Optional.ofNullable(beerMapper.beerToDTO(beerRepository.findById(uuid).orElse(null)));
    }

    @Override
    public List<BeerDTO> getAllBeers() {
        return beerRepository.findAll().stream().map(beerMapper::beerToDTO).toList();
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {
        Beer savedBeer = beerRepository.save(beerMapper.dtoToBeer(beer));
        return beerMapper.beerToDTO(savedBeer);
    }

    @Override
    public Optional<BeerDTO> updateBeerById(UUID id, BeerDTO beerDto) {
        AtomicReference<Optional<BeerDTO>> atomicReferenceBeerToUpdate = new AtomicReference<>(Optional.empty());

        beerRepository.findById(id).ifPresent(beer -> {
            beer.setBeerName(beerDto.getBeerName());
            beer.setUpc(beerDto.getUpc());
            beer.setPrice(beerDto.getPrice());
            beer.setBeerStyle(beerDto.getBeerStyle());
            beer.setQuantityOnHand(beerDto.getQuantityOnHand());
            atomicReferenceBeerToUpdate.set(Optional.of(beerMapper.beerToDTO(beerRepository.save(beer))));
        });

        return atomicReferenceBeerToUpdate.get();
    }

    @Override
    public BeerDTO patchBeerById(UUID id, BeerDTO beerDto) {
        Optional<Beer> beerToPatch = beerRepository.findById(id);

        Beer beer = beerToPatch.get();
        if (StringUtils.hasText(beerDto.getBeerName())) beer.setBeerName(beer.getBeerName());
        if (StringUtils.hasText(beerDto.getUpc())) beer.setUpc(beer.getUpc());
        if (beerDto.getPrice() != null) beer.setPrice(beer.getPrice());
        if (beerDto.getBeerStyle() != null) beer.setBeerStyle(beer.getBeerStyle());
        if (beerDto.getQuantityOnHand() != null) beer.setQuantityOnHand(beer.getQuantityOnHand());
        return beerMapper.beerToDTO(beerRepository.save(beer));
    }

    @Override
    public void deleteBeerById(UUID id) {
        beerRepository.deleteById(id);
    }

}
