package com.example.demo.services;

import com.example.demo.dto.Quote;
import com.example.demo.services.cloud.QuoteServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Order(3)
@Service
@AllArgsConstructor
public class QuoteService implements QuoteGetter {

    private static final String METHOD_NAME = "getQuote";
    private static final String FORMAT = "json";
    private static final String LANGUAGE = "ru";

    private final QuoteServiceClient quoteServiceClient;
    private final CacheService cacheService;

    /**
     * Получение рандомной цитаты.
     * @return цитата.
     */
    @Override
    public Quote getQuote() {
        try {
            var quote = quoteServiceClient.getRandomQuote(METHOD_NAME, FORMAT, LANGUAGE);

            quote.setSource(quote.getQuoteAuthor());
            cacheService.saveQuote(quote);

            return quote;
        } catch (Exception e) {
            return null;
        }
    }

}
