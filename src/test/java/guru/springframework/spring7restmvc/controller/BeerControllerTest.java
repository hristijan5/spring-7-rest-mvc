package guru.springframework.spring7restmvc.controller;

import guru.springframework.spring7restmvc.model.Beer;
import guru.springframework.spring7restmvc.services.BeerService;
import guru.springframework.spring7restmvc.services.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
@ExtendWith(MockitoExtension.class)
class BeerControllerTest {

    public static final String BEER_RESOURCE_PATH = "/api/v1/beer";
    private static final String BEER_RESOURCE_BY_ID = BEER_RESOURCE_PATH + "/{beerId}";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    JsonMapper mapper;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<Beer> beerArgumentCaptor;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void getAllBeers() throws Exception {
        given(beerService.getAllBeers()).willReturn(beerServiceImpl.getAllBeers());

        mockMvc.perform(get(BEER_RESOURCE_PATH).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    void getBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().getFirst();

        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BEER_RESOURCE_BY_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(content().json("{'id':'" + testBeer.getId() + "'}"))
                .andExpect(jsonPath("$.id").value(testBeer.getId().toString()))
                .andExpect(jsonPath("$.upc").value(testBeer.getUpc()))
                .andExpect(jsonPath("$.beerName").value(testBeer.getBeerName()));
    }

    @Test
    void getBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(BEER_RESOURCE_BY_ID, UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeer() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().getFirst();
        testBeer.setId(null);
        testBeer.setVersion(null);

        given(beerService.createBeer(any(Beer.class))).willReturn(beerServiceImpl.getAllBeers().getFirst());

        mockMvc.perform(post(BEER_RESOURCE_PATH)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBeer)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().getFirst();

        mockMvc.perform(put(BEER_RESOURCE_BY_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testBeer)))
                .andExpect(status().isOk());

        verify(beerService).updateBeerById(any(UUID.class), any(Beer.class));
    }

    @Test
    void patchBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().getFirst();

        HashMap<String, String> beerPatch = new HashMap<>();
        beerPatch.put("beerName", "Updated Beer Name");

        mockMvc.perform(patch(BEER_RESOURCE_BY_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(beerPatch)))
                .andExpect(status().isOk());

        verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

        assertEquals(testBeer.getId(), uuidArgumentCaptor.getValue());
        assertEquals(beerPatch.get("beerName"), beerArgumentCaptor.getValue().getBeerName());
    }

    @Test
    void deleteBeerById() throws Exception {
        Beer testBeer = beerServiceImpl.getAllBeers().getFirst();

        mockMvc.perform(delete(BEER_RESOURCE_BY_ID, testBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
//        verify(beerService).deleteBeerById(testBeer.getId());

        assertEquals(testBeer.getId(), uuidArgumentCaptor.getValue());
    }

}
