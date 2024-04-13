package com.example.demo;

import com.example.demo.dto.Quote;
import com.example.demo.services.CacheService;
import com.example.demo.services.QuoteService;
import com.example.demo.services.cloud.QuoteServiceClient;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ActiveProfiles("test")
public class QuoteServiceTests {

    private EasyRandom generator = new EasyRandom();
    @Mock
    QuoteServiceClient quoteServiceClient;
    @Mock
    CacheService cacheService;
    @InjectMocks
    QuoteService quoteService;

    @Test
    void getQuoteTest() {
        var quote = Quote.builder().quoteAuthor(generator.nextObject(String.class))
                .quoteText(generator.nextObject(String.class)).build();

        Mockito.when(quoteServiceClient.getRandomQuote(anyString(), anyString(), anyString())).thenReturn(quote);
        Mockito.doNothing().when(cacheService).saveQuote(quote);

        Assertions.assertTrue(Objects.nonNull(quoteService.getQuote()));
        Assertions.assertTrue(Objects.nonNull(quoteService.getQuote().getSource()));
    }

}
