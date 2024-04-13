package com.example.demo.services;

import com.example.demo.dto.ExternalServiceRequest;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.MovieRequest;
import com.example.demo.dto.Quote;
import com.example.demo.exception.ElementExistException;
import com.example.demo.jpa.MovieRepository;
import com.example.demo.services.mapping.MovieMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сервис взаимодействия со списком фильмов.
 * @param <T> формат запросов к внешнему сервису.
 */
@Slf4j
@AllArgsConstructor
public abstract class MovieService<T extends ExternalServiceRequest> implements QuoteGetter {

    MovieRepository repository;
    MovieMapper mapper;
    ServiceRequestDirector director;
    CacheService cacheService;

    /**
     * Получение рекомендаций от удаленного ресурса.
     * @param request параметры запроса к удаленному ресурсу.
     * @return список удовлетворяющих условиям фильмов.
     */
    public abstract List<MovieDto> getRecommendationsFromRemoteService(T request);

    /**
     * Получение слогана случайного фильма с удаленного ресурса.
     * @return слоган.
     */
    public abstract Quote getRandomSlogan(T request);

    /**
     * Получение рейтингов фильмов из удаленного сервиса.
     * @return слоган.
     */
    public abstract void getAndEnrichMovieRatings(T request, List<MovieDto> dtoList);

    /**
     * Шаблоннный метод поиска фильмов.
     * @param isLocalSearch признак необходимости искать в уже добавленных или на удаленном сервисе.
     * @param request параметры запроса для поиска.
     * @return список удовлетворяющих условиям фильмов.
     */
    public List<MovieDto> searchRecommendations(Boolean isLocalSearch, MovieRequest request) {
        List<MovieDto> result;
        List<MovieDto> forSaveList;

        if (isLocalSearch) {
            result = findMoviesInLocalList(request);

            var movieIdsForSearch = result.stream().filter(i -> Objects.isNull(i.getRating()))
                    .map(MovieDto::getMovieId).collect(Collectors.toList());

            if (!movieIdsForSearch.isEmpty()) {
                getAndEnrichMovieRatings(director.buildRatingRequest(movieIdsForSearch), result);
            }

            forSaveList = result.stream().filter(i -> movieIdsForSearch.contains(i.getMovieId()))
                    .collect(Collectors.toList());
        } else {
            result = getRecommendationsFromRemoteService(director.buildMovieRequest(request));
            forSaveList = result;
        }

        cacheService.saveRatings(forSaveList);

        return result;
    }

    /**
     * Поиск фильмов в локальном списке.
     * @param request параметры запроса.
     * @return список фильмов, подходящих условиям.
     */
    private List<MovieDto> findMoviesInLocalList(MovieRequest request) {
        return mapper.entitiesToDtoList(repository.findAllWithPaging(request)).stream()
                .filter(dto -> (Objects.isNull(request.getMinRating())
                        || (Objects.nonNull(dto.getRating()) && request.getMinRating() <= dto.getRating()))
                        && (Objects.isNull(request.getMaxRating())
                        || (Objects.nonNull(dto.getRating()) && request.getMaxRating() >= dto.getRating())))
                .limit(request.getRecordCount())
                .collect(Collectors.toList());
    }

    /**
     * Метод добавления фильма в список для просмотра.
     * @param request параметры запроса на добавление.
     */
    public void addMovieInDatabase(MovieDto request) {
        if (repository.existsByExternalId(request.getMovieId())) {
            throw new ElementExistException(true);
        }

        repository.save(mapper.dtoToEntity(request));
    }

    /**
     * Метод простановки отметки Просмотрено.
     * @param id идентификатор фильма в списке.
     */
    public void markMovieAsWatched(Long id) {
        var movieEntity = repository.findByExternalId(id).orElseThrow(() -> new ElementExistException(false));

        movieEntity.setIsWatched(true);

        repository.save(movieEntity);
    }

    /**
     * Шаблоннный метод поиска слогана случайного фильма.
     * @return слоган.
     */
    @Override
    public Quote getQuote() {
        try {
            var slogan = getRandomSlogan(director.buildRandomSloganRequest());

            cacheService.saveQuote(slogan);

            return slogan;
        } catch (Exception e) {
            log.error(e.getMessage());

            return null;
        }
    }

}
