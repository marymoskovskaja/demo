package com.example.demo;

import com.example.demo.controllers.MovieController;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.MovieRequest;
import com.example.demo.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jeasy.random.EasyRandom;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@WebMvcTest(MovieController.class)
public class MovieRestControllerTests {

    private EasyRandom generator = new EasyRandom();

    @Autowired
    private MockMvc mvc;
    @MockBean
    MovieService movieService;

    @BeforeEach
    void setUp() {
        Mockito.when(movieService.searchRecommendations(anyBoolean(), any(MovieRequest.class)))
                .thenReturn(generator.objects(MovieDto.class, generator.nextInt(10) + 1)
                        .collect(Collectors.toList()));
    }

    @Test
    public void emptyParamAndBodyGetRecommendationsTest() throws Exception {
        mvc.perform(post("/movies/get-recommendations").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void emptyBodyRequestGetRecommendationsTest() throws Exception {
        mvc.perform(post("/movies/get-recommendations").contentType(MediaType.APPLICATION_JSON)
                        .param("isLocalSearch", String.valueOf(generator.nextBoolean())))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void wrongBodyRequestGetRecommendationsTest() throws Exception {
        mvc.perform(post("/movies/get-recommendations").contentType(MediaType.APPLICATION_JSON)
                        .param("isLocalSearch", String.valueOf(generator.nextBoolean()))
                        .content(new ObjectMapper().writeValueAsString(generator.nextObject(MovieRequest.class))))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void rightBodyRequestGetRecommendationsTest() throws Exception {
        mvc.perform(post("/movies/get-recommendations").contentType(MediaType.APPLICATION_JSON)
                        .param("isLocalSearch", String.valueOf(generator.nextBoolean()))
                        .content(new ObjectMapper().writeValueAsString(
                                MovieRequest.builder().recordCount(10).minRating(1.0).maxRating(10.0)
                                        .minYear(2010L).maxYear(2020L).minLength(80L).maxLength(120L).build())))
                .andExpect(status().isOk());
    }


}
