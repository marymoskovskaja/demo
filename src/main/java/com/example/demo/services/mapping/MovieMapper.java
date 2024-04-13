package com.example.demo.services.mapping;

import com.example.demo.domain.MovieEntity;
import com.example.demo.dto.KpMovieResponse;
import com.example.demo.dto.MovieDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
@DecoratedWith(MovieMapperDecorator.class)
public interface MovieMapper {

    @Mapping(source = "externalId", target = "movieId")
    MovieDto entityToDto(MovieEntity entity);

    List<MovieDto> entitiesToDtoList(List<MovieEntity> entity);

    @Mapping(source = "movieId", target = "externalId")
    @Mapping(target = "isWatched", expression = "java(false)")
    MovieEntity dtoToEntity(MovieDto dto);

    List<MovieEntity> dtoListToEntities(List<MovieDto> dtoList);

    @Mapping(source = "id", target = "movieId")
    @Mapping(source = "name", target = "movieName")
    @Mapping(source = "movieLength", target = "length")
    @Mapping(source = "rating.kp", target = "rating")
    MovieDto kpResponseToDto(KpMovieResponse response);
    List<MovieDto> kpResponseListToDtoList(List<KpMovieResponse> responseList);

}
