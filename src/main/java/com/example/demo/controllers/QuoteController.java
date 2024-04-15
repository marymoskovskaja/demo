package com.example.demo.controllers;

import com.example.demo.dto.Quote;
import com.example.demo.services.QuoteGetter;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Контроллер для работы с цитатами.
 */
@RestController
@RequestMapping("/quote")
@AllArgsConstructor
public class QuoteController {

    private final List<QuoteGetter> quoteGetterList;

    @GetMapping(value = "/get-quote")
    @ApiOperation(value = "Получить цитату дня")
    public Quote getQuoteOfTheDay() {
        Quote quote = null;

        for (var getter: quoteGetterList) {
            quote = getter.getQuote();

            if (Objects.nonNull(quote)) {
                break;
            }
        }

        return Optional.ofNullable(quote).orElse(Quote.builder()
                .quoteText("Извините, сегодня ничего не нашлось =(").build());
    }

}
