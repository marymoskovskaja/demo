package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

/**
 * Информация о фильме. Ответ кинопоиска.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KpMovieResponse {

    /**
     * Идентификатор фильма.
     */
    private Long id;
    private String name;
    private String description;
    private Long year;
    private List<ItemName> countries;
    private Long movieLength;
    private Rating rating;
    private String slogan;

}
