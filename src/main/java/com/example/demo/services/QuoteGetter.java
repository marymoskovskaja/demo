package com.example.demo.services;

import com.example.demo.dto.Quote;

/**
 * Общий интерфейс для сервисов, возвращающих цитаты.
 */
public interface QuoteGetter {

    /**
     * Метод получения цитаты.
     * @return цитата.
     */
    Quote getQuote();

}
