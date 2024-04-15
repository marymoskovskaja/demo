package com.example.demo.controllers;

import com.example.demo.dto.MovieDto;
import com.example.demo.dto.MovieRequest;
import com.example.demo.services.MovieService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Контроллер для работы с фильмами.
 */
@RestController
@RequestMapping("/movies")
@AllArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping(value = "/get-recommendations")
    @ApiOperation(value = "Выбираем фильм на вечер")
    public List<MovieDto> getRecommendations(@RequestParam Boolean isLocalSearch,
                                             @Valid @RequestBody MovieRequest request) {
        return movieService.searchRecommendations(isLocalSearch, request);
    }

    @PostMapping("/add-movie")
    @ApiOperation(value = "Это мы смотрим (добавить в личный список)")
    public void addMovieToPersonalList(@RequestBody MovieDto request) {
        movieService.addMovieInDatabase(request);
    }

    @PutMapping("/mark-as-watched")
    @ApiOperation(value = "Отметить как просмотренное")
    public void markAsWatched(@RequestParam Long id) {
        movieService.markMovieAsWatched(id);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "Передумал смотреть (удалить из личного списка)")
    public void delete(@RequestParam Long id) {
        movieService.deleteFromList(id);
    }

}
