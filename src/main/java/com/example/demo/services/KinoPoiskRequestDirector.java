package com.example.demo.services;

import com.example.demo.dto.KpServiceRequest;
import com.example.demo.dto.MovieRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@ConditionalOnProperty(prefix = "services", name = "movie-service", havingValue = "kp")
public class KinoPoiskRequestDirector implements ServiceRequestDirector {
    private static final List<String> MOVIE_RESPONSE_FIELDS = List.of(
            "id", "name", "description", "year", "rating", "countries", "movieLength");
    private static final List<String> SLOGAN_RESPONSE_FIELDS = List.of("name", "slogan");
    private static final List<String> RATING_RESPONSE_FIELDS = List.of("id", "rating");

    /**
     * Формирование универсального запроса на поиск фильмов.
     * @param movieRequest запрос с клиента.
     * @return запрос для кинопоиска.
     */
    @Override
    public KpServiceRequest buildMovieRequest(MovieRequest movieRequest) {
        return KpServiceRequest.builder()
                .limit(movieRequest.getRecordCount())
                .page(1)
                .selectFields(MOVIE_RESPONSE_FIELDS)
                .movieLength(getRangeParam(movieRequest.getMinLength(), movieRequest.getMaxLength()))
                .year(getRangeParam(movieRequest.getMinYear(), movieRequest.getMaxYear()))
                .rating(getRangeParam(movieRequest.getMinRating(), movieRequest.getMaxRating()))
                .isSeries(false)
                .build();
    }

    /**
     * Формирование параметра запроса в формате "x-y".
     * @param from левая граница.
     * @param to правая граница.
     * @return параметр запроса для ограничения выборки.
     */
    private String getRangeParam(Object from, Object to) {
        return Stream.of(from, to).filter(Objects::nonNull)
                .map(Object::toString).collect(Collectors.joining("-"));
    }

    @Override
    public KpServiceRequest buildRandomSloganRequest() {
        return KpServiceRequest.builder()
                .notNullFields(SLOGAN_RESPONSE_FIELDS)
                .isSeries(false)
                .build();
    }

    @Override
    public KpServiceRequest buildRatingRequest(List ids) {
        return KpServiceRequest.builder()
                .selectFields(RATING_RESPONSE_FIELDS)
                .id(ids)
                .build();
    }

}
