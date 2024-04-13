package com.example.demo;

import com.example.demo.controllers.QuoteController;
import com.example.demo.dto.Quote;
import com.example.demo.services.CacheService;
import com.example.demo.services.MovieService;
import com.example.demo.services.QuoteService;
import com.example.demo.services.cloud.QuoteServiceClient;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
@ActiveProfiles("test")
public class QuoteControllerTests {

    private final static String TEST_KEY = "test quote";

    private EasyRandom generator = new EasyRandom();

    @Mock
    private CacheService cacheServiceMock;
    @Mock
    private MovieService movieServiceMock;
    @Mock
    private MovieService quoteServiceMock;
    @Mock
    private QuoteServiceClient quoteServiceClientMock;
    @Spy
    @InjectMocks
    private QuoteService quoteServiceSpy;
    @InjectMocks
    private QuoteController controller;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @BeforeEach
    public void setUp() {
        redisTemplate.delete(TEST_KEY);

        controller = new QuoteController(List.of(cacheServiceMock, movieServiceMock, quoteServiceSpy));
    }

    @Test
    void gqtQuoteReturnDefaultAnswerTest() {
        Mockito.when(cacheServiceMock.getQuote()).thenReturn(null);
        Mockito.when(movieServiceMock.getQuote()).thenReturn(null);
        Mockito.when(quoteServiceSpy.getQuote()).thenReturn(null);

        var quote = controller.getQuoteOfTheDay();

        Assertions.assertTrue(Objects.nonNull(quote));
        Assertions.assertTrue(StringUtils.isNotBlank(quote.getQuoteText()));
    }

    @Test
    void getQuoteReturnsConstantAnswerTest() {
        Mockito.when(cacheServiceMock.getQuote()).thenReturn(generator.nextObject(Quote.class));
        Mockito.when(movieServiceMock.getQuote()).thenReturn(generator.nextObject(Quote.class));
        Mockito.when(quoteServiceSpy.getQuote()).thenReturn(generator.nextObject(Quote.class));

        Assertions.assertEquals(controller.getQuoteOfTheDay(), controller.getQuoteOfTheDay());
    }

    @Test
    void getQuoteSaveInCache() {
        var firstQuote = generator.nextObject(Quote.class);
        var secondQuote = generator.nextObject(Quote.class);

        Mockito.doAnswer(invocationOnMock -> redisTemplate.opsForValue().get(TEST_KEY))
                .when(cacheServiceMock).getQuote();
        Mockito.doAnswer(invocationOnMock -> {
            redisTemplate.opsForValue().set(TEST_KEY, invocationOnMock.getArgument(0));

            return null;
        }).when(cacheServiceMock).saveQuote(any(Quote.class));

        Mockito.when(movieServiceMock.getQuote()).thenReturn(null);
        Mockito.when(quoteServiceClientMock.getRandomQuote(anyString(), anyString(), anyString()))
                .thenReturn(firstQuote);

        Assertions.assertEquals(firstQuote, controller.getQuoteOfTheDay());

        Mockito.when(quoteServiceClientMock.getRandomQuote(anyString(), anyString(), anyString()))
                .thenReturn(secondQuote);

        Assertions.assertEquals(firstQuote, controller.getQuoteOfTheDay());
    }

    @Test
    void getQuoteServiceOrderIsCorrect() {
        var quoteController = new QuoteController(List.of(cacheServiceMock, movieServiceMock, quoteServiceMock));
        var firstQuote = generator.nextObject(Quote.class);

        Mockito.when(cacheServiceMock.getQuote()).thenReturn(firstQuote);
        Mockito.when(movieServiceMock.getQuote()).thenReturn(null);
        Mockito.when(quoteServiceMock.getQuote()).thenReturn(null);

        Assertions.assertEquals(firstQuote, quoteController.getQuoteOfTheDay());

        Mockito.verify(cacheServiceMock, Mockito.times(1)).getQuote();
        Mockito.verify(movieServiceMock, Mockito.times(0)).getQuote();
        Mockito.verify(quoteServiceMock, Mockito.times(0)).getQuote();

        var secondQuote = generator.nextObject(Quote.class);

        Mockito.when(cacheServiceMock.getQuote()).thenReturn(null);
        Mockito.when(movieServiceMock.getQuote()).thenReturn(secondQuote);
        Mockito.when(quoteServiceMock.getQuote()).thenReturn(null);

        Assertions.assertEquals(secondQuote, quoteController.getQuoteOfTheDay());

        Mockito.verify(cacheServiceMock, Mockito.times(2)).getQuote();
        Mockito.verify(movieServiceMock, Mockito.times(1)).getQuote();
        Mockito.verify(quoteServiceMock, Mockito.times(0)).getQuote();

        var thirdQuote = generator.nextObject(Quote.class);

        Mockito.when(cacheServiceMock.getQuote()).thenReturn(null);
        Mockito.when(movieServiceMock.getQuote()).thenReturn(null);
        Mockito.when(quoteServiceMock.getQuote()).thenReturn(thirdQuote);

        Assertions.assertEquals(thirdQuote, quoteController.getQuoteOfTheDay());

        Mockito.verify(cacheServiceMock, Mockito.times(3)).getQuote();
        Mockito.verify(movieServiceMock, Mockito.times(2)).getQuote();
        Mockito.verify(quoteServiceMock, Mockito.times(1)).getQuote();
    }

}
