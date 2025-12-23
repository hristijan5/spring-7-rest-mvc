package guru.springframework.spring7restmvc.bootstrap;

import guru.springframework.spring7restmvc.entities.Beer;
import guru.springframework.spring7restmvc.entities.Customer;
import guru.springframework.spring7restmvc.model.BeerStyle;
import guru.springframework.spring7restmvc.repositories.BeerRepository;
import guru.springframework.spring7restmvc.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeers();
        loadCustomers();
    }

    private void loadBeers() {
        if (beerRepository.count() > 0) return;
        beerRepository.save(Beer.builder()
                .upc("123")
                .beerName("Heineken")
                .beerStyle(BeerStyle.LAGER)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(80)
                .price(BigDecimal.valueOf(0.99))
                .build()
        );
        beerRepository.save(Beer.builder()
                .upc("456")
                .beerName("Franziskaner")
                .beerStyle(BeerStyle.WHEAT)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(100)
                .price(BigDecimal.valueOf(1.49))
                .build()
        );
        beerRepository.save(Beer.builder()
                .upc("789")
                .beerName("Guinness")
                .beerStyle(BeerStyle.STOUT)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .quantityOnHand(50)
                .price(BigDecimal.valueOf(1.99))
                .build()
        );

    }

    private void loadCustomers() {
        if (customerRepository.count() > 0) return;
        customerRepository.save(Customer.builder()
                .customerName("username1")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now()).build());
        customerRepository.save(Customer.builder()
                .customerName("username2")
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build());
    }

}
