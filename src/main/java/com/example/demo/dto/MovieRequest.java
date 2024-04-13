package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class MovieRequest {

    @NotNull(message = "Необходимо указать количество записей для выборки!")
    @Min(value = 1, message = "Количество записей для выборки должно быть больше 0!")
    private Integer recordCount;
    @NotNull
    @Min(value = 1874, message = "Год должен быть в диапазоне от 1874 до 2050!")
    private Long minYear;
    @NotNull
    @Max(value = 2050, message = "Год должен быть в диапазоне от 1874 до 2050!")
    private Long maxYear;
    @NotNull
    @Min(value = 1, message = "Фильм не может быть короче 1 минуты!")
    private Long minLength;
    @NotNull
    @Max(value = 2880, message = "Фильм не может быть длиннее 2880 минут!")
    private Long maxLength;
    @NotNull
    @Min(value = 0, message = "Рейтинг должен быть в диапазоне от 0 до 10!")
    private Double minRating;
    @NotNull
    @Max(value = 10, message = "Рейтинг должен быть в диапазоне от 0 до 10!")
    private Double maxRating;

}
