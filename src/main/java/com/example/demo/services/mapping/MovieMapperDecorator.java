package com.example.demo.services.mapping;

import com.example.demo.domain.MovieEntity;
import com.example.demo.dto.ItemName;
import com.example.demo.dto.KpMovieResponse;
import com.example.demo.dto.MovieDto;
import com.example.demo.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MovieMapperDecorator implements MovieMapper {

    @Autowired
    MovieMapper mapper;
    @Autowired
    CacheService cacheService;

    @Override
    public MovieDto entityToDto(MovieEntity entity) {
        var dto = mapper.entityToDto(entity);

        dto.setLengthDescription(movieLengthToString(entity.getLength()));
        dto.setRating(cacheService.getRating(entity.getExternalId(), entity.getYear()));

        return dto;
    }

    private String movieLengthToString(Long length) {
        return Objects.nonNull(length) ? String.format("%s ч %s мин", length / 60, length % 60) : null;
    }

    @Override
    public List<MovieDto> entitiesToDtoList(List<MovieEntity> entityList) {
        return entityList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    @Override
    public MovieEntity dtoToEntity(MovieDto dto) {
        return mapper.dtoToEntity(dto);
    }

    @Override
    public List<MovieEntity> dtoListToEntities(List<MovieDto> dtoList) {
        return mapper.dtoListToEntities(dtoList);
    }

    @Override
    public MovieDto kpResponseToDto(KpMovieResponse response) {
        var dto = mapper.kpResponseToDto(response);

        dto.setLengthDescription(movieLengthToString(response.getMovieLength()));
        dto.setCountry(response.getCountries().stream().map(ItemName::getName).findFirst().orElse(null));

        return dto;
    }

    @Override
    public List<MovieDto> kpResponseListToDtoList(List<KpMovieResponse> responseList) {
        return responseList.stream().map(this::kpResponseToDto).collect(Collectors.toList());
    }

}
