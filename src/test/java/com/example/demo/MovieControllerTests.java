package com.example.demo;

import com.example.demo.controllers.MovieController;
import com.example.demo.domain.MovieEntity;
import com.example.demo.dto.ExternalServiceRequest;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.MovieRequest;
import com.example.demo.dto.Quote;
import com.example.demo.exception.ElementExistException;
import com.example.demo.jpa.MovieRepository;
import com.example.demo.services.CacheService;
import com.example.demo.services.MovieService;
import com.example.demo.services.ServiceRequestDirector;
import com.example.demo.services.mapping.MovieMapper;
import com.example.demo.services.mapping.MovieMapperImpl_;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@SpringBootTest
@ActiveProfiles("test")
public class MovieControllerTests {

    private EasyRandom generator = new EasyRandom();
    private MovieController controller;

    @Mock
    private MovieRepository repository;
    @Mock
    private ServiceRequestDirector director;
    @Mock
    private CacheService cacheService;
    @Spy
    private MovieMapper mapperImpl = new MovieMapperImpl_();
    @InjectMocks
    private MovieMapper mapper = Mappers.getMapper(MovieMapper.class);

    @BeforeEach
    void setUp() {
        var movieService = new MovieService(repository, mapper, director, cacheService) {

            @Override
            public List<MovieDto> getRecommendationsFromRemoteService(ExternalServiceRequest request) {
                return generator.objects(MovieDto.class, generator.nextInt(10) + 1)
                        .collect(Collectors.toList());
            }

            @Override
            public Quote getRandomSlogan(ExternalServiceRequest request) {
                return null;
            }

            @Override
            public void getAndEnrichMovieRatings(ExternalServiceRequest request, List list) {
            }
        };

        controller = new MovieController(movieService);
    }

    @Test
    void getRecommendationsLocalListRequestTest() {
        Mockito.when(repository.findAllWithPaging(any(MovieRequest.class))).thenReturn(
                generator.objects(MovieEntity.class, generator.nextInt(10) + 1)
                        .collect(Collectors.toList()));
        Mockito.when(cacheService.getRating(anyLong(),anyLong())).thenReturn(generator.nextDouble());

        var localList = controller.getRecommendations(true, MovieRequest.builder()
                .recordCount(generator.nextInt(10) + 1)
                .minYear(Long.MIN_VALUE).maxYear(Long.MAX_VALUE)
                .minLength(Long.MIN_VALUE).maxLength(Long.MAX_VALUE)
                .minRating(Double.MIN_VALUE).maxRating(Double.MAX_VALUE).build());

        Assertions.assertFalse(CollectionUtils.isEmpty(localList));

        Mockito.verify(repository, Mockito.times(1)).findAllWithPaging(any(MovieRequest.class));
    }

    @Test
    void getRecommendationsExternalListRequestTest() {
        var externalList = controller.getRecommendations(false, MovieRequest.builder()
                .recordCount(generator.nextInt(10) + 1)
                .minYear(Long.MIN_VALUE).maxYear(Long.MAX_VALUE)
                .minLength(Long.MIN_VALUE).maxLength(Long.MAX_VALUE)
                .minRating(Double.MIN_VALUE).maxRating(Double.MAX_VALUE).build());

        Assertions.assertFalse(CollectionUtils.isEmpty(externalList));

        Mockito.verify(repository, Mockito.times(0)).findAllWithPaging(any(MovieRequest.class));
    }

    @Test
    void addMovieNotExistsTest() {
        Assertions.assertDoesNotThrow(() -> controller.addMovieToPersonalList(generator.nextObject(MovieDto.class)));

        Mockito.verify(repository, Mockito.times(1)).save(any(MovieEntity.class));
    }

    @Test
    void addMovieAlreadyExistsTest() {
        Mockito.when(repository.existsByExternalId(anyLong())).thenReturn(true);

        Assertions.assertThrows(ElementExistException.class, () -> controller
                .addMovieToPersonalList(generator.nextObject(MovieDto.class)));

        Mockito.verify(repository, Mockito.times(0)).save(any(MovieEntity.class));
    }

    @Test
    void markAsWatchedMovieWorkTest() {
        Mockito.when(repository.getMovieFromExternalIdOrThrow(anyLong()))
                .thenReturn(generator.nextObject(MovieEntity.class));

        Assertions.assertDoesNotThrow(() -> controller.markAsWatched(generator.nextLong()));

        Mockito.verify(repository, Mockito.times(1)).save(any(MovieEntity.class));
    }

    @Test
    void deleteMovieWorkTest() {
        var entity = generator.nextObject(MovieEntity.class);

        Mockito.when(repository.getMovieFromExternalIdOrThrow(anyLong())).thenReturn(entity);

        Assertions.assertDoesNotThrow(() -> controller.delete(entity.getExternalId()));

        Mockito.verify(repository, Mockito.times(1)).delete(any(MovieEntity.class));
    }

}
