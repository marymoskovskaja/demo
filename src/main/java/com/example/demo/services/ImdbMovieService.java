package com.example.demo.services;

import com.example.demo.dto.ImdbServiceRequest;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.Quote;
import com.example.demo.jpa.MovieRepository;
import com.example.demo.services.mapping.MovieMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Сервис взаимодействия с IMDB.
 */
@Slf4j
@Service
@Order(2)
@ConditionalOnProperty(prefix = "services", name = "movie-service", havingValue = "imdb")
public class ImdbMovieService extends MovieService<ImdbServiceRequest> {
    public ImdbMovieService(MovieRepository repository, MovieMapper mapper,
                            ImdbRequestDirector director, CacheService cacheService) {
        super(repository, mapper, director, cacheService);
    }

    @Override
    public List<MovieDto> getRecommendationsFromRemoteService(ImdbServiceRequest request) {
        log.error("Получение рекомендаций IMDB пока недоступно!");

        return Collections.emptyList();
    }

    @Override
    public Quote getRandomSlogan(ImdbServiceRequest request) {
        return null;
    }

    @Override
    public void getAndEnrichMovieRatings(ImdbServiceRequest request, List<MovieDto> dtoList) {
        log.error("IMDB пока недоступен!");
    }

    @Override
    public Quote getQuote() {
        log.error("IMDB пока недоступен!");

        return null;
    }

}
