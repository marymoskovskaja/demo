package com.example.demo.services;

import com.example.demo.dto.MovieDto;
import com.example.demo.dto.Quote;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
@Order(1)
public class CacheService implements QuoteGetter {

    private static final String QUOTE_KEY = "Quote of the day";

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Сброс кэша redis. Автоматически запускается ежедневно в полночь.
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void clearCache() {
        Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().flushAll();
        log.info("Был произведен сброс кэша!");
    }

    /**
     * Получить сохраненную в кэше цитату.
     * @return цитата.
     */
    @Override
    public Quote getQuote() {
        return (Quote) redisTemplate.opsForValue().get(QUOTE_KEY);
    }

    /**
     * Сохранить цитату.
     * @param quote цитата.
     */
    public void saveQuote(Quote quote) {
        redisTemplate.opsForValue().set(QUOTE_KEY, quote);
    }

    /**
     * Сохранение рейтингов запрашиваемых фильмов в кэше.
     * @param result информация о фильмах.
     */
    public void saveRatings(List<MovieDto> result) {
        result.forEach(item -> redisTemplate.opsForValue().set(
                String.format("%s-%s", item.getMovieId(), item.getYear()), item.getRating()));
    }

    /**
     * Получение рейтинга из кэша.
     * @param movieId идентификатор фильма.
     * @param year год.
     * @return рейтинг.
     */
    public Double getRating(Long movieId, Long year) {
        var rating = redisTemplate.opsForValue().get(String.format("%s-%s", movieId, year));

        return Objects.nonNull(rating) ? (Double) rating : null;
    }

}
