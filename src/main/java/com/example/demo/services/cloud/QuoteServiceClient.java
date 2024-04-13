package com.example.demo.services.cloud;

import com.example.demo.dto.Quote;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "quote-api", url = "${services.quote-address}")
public interface QuoteServiceClient {

    @GetMapping("/")
    Quote getRandomQuote(@RequestParam("method") String methodName,
                         @RequestParam("format") String format,
                         @RequestParam("lang") String language);

}
