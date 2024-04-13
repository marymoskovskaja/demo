package com.example.demo.dto;

import lombok.Data;

@Data
public class MovieDto {

    private Long movieId;
    private String movieName;
    private String description;
    private Integer year;
    private String country;
    private Long length;
    private String lengthDescription;
    private Double rating;

}
