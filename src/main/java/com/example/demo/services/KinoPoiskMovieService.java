package com.example.demo.services;

import com.example.demo.dto.KpMovieResponse;
import com.example.demo.dto.KpServiceRequest;
import com.example.demo.dto.MovieDto;
import com.example.demo.dto.Quote;
import com.example.demo.jpa.MovieRepository;
import com.example.demo.services.cloud.KinoPoiskClient;
import com.example.demo.services.mapping.MovieMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Сервис взаимодействия с Кинопоиском.
 */
@Service
@Order(2)
@ConditionalOnProperty(prefix = "services", name = "movie-service", havingValue = "kp")
public class KinoPoiskMovieService extends MovieService<KpServiceRequest> {

    private final KinoPoiskClient client;

    public KinoPoiskMovieService(MovieRepository repository, MovieMapper mapper,
                                 KinoPoiskRequestDirector director, CacheService cacheService,
                                 KinoPoiskClient client) {
        super(repository, mapper, director, cacheService);
        this.client = client;
    }

    /**
     * Получение рекомендаций согласно заданным параметрам поиска.
     * @param request параметры запроса к удаленному ресурсу.
     * @return список фильмов.
     */
    @Override
    public List<MovieDto> getRecommendationsFromRemoteService(KpServiceRequest request) {
        return mapper.kpResponseListToDtoList(client.getMoviesByFilters(request.toMap()).getDocs());
    }

    /**
     * Получение цитаты (слогана фильма).
     * @return цитата.
     */
    @Override
    public Quote getRandomSlogan(KpServiceRequest request) {
        var kpResponse = client.getRandomSlogan(request.toMap());

        if (Objects.nonNull(kpResponse) && StringUtils.isNotBlank(kpResponse.getSlogan())) {
            return Quote.builder().quoteText(kpResponse.getSlogan()).source(kpResponse.getName()).build();
        }

        return null;
    }

    @Override
    public void getAndEnrichMovieRatings(KpServiceRequest request, List<MovieDto> dtoList) {
        var ratingMap = client.getMoviesByFilters(request.toMap()).getDocs().stream()
                .collect(Collectors.toMap(KpMovieResponse::getId, KpMovieResponse::getRating, (x, y) -> x));

        dtoList.forEach(dto -> {
            if (ratingMap.containsKey(dto.getMovieId())) {
                dto.setRating(ratingMap.get(dto.getMovieId()).getKp());
            }
        });
    }

}
