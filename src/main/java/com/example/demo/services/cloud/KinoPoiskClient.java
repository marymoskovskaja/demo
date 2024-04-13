package com.example.demo.services.cloud;

import com.example.demo.dto.KpMovieResponse;
import com.example.demo.dto.KpResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Клиент для работы с Кинопоиском.
 */
@FeignClient(value = "kp-api", url = "${services.kp-address}")
public interface KinoPoiskClient {

    @GetMapping("/movie")
    KpResponse getMoviesByFilters(@RequestParam Map<String, String> request);

    @GetMapping("/movie/random")
    KpMovieResponse getRandomSlogan(@RequestParam Map<String, String> request);

}
