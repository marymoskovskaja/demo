package com.example.demo.jpa;

import com.example.demo.domain.MovieEntity;
import com.example.demo.dto.MovieRequest;
import com.example.demo.exception.ElementExistException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long>, JpaSpecificationExecutor<MovieEntity> {

    /**
     * Поиск в локальном списке фильмов согласно заданным фильтрам.
     * @param request параметры запроса.
     * @return подходящие фильмы.
     */
    default List<MovieEntity> findAllWithPaging(MovieRequest request) {
        var specification = MovieSpecifications.getMovieSearchSpecification(request);

        if (Objects.nonNull(request.getMinRating()) || Objects.nonNull(request.getMaxRating())) {
            return findAll(specification);
        }

        return findAll(specification, PageRequest.of(0, request.getRecordCount())).getContent();
    }

    Boolean existsByExternalId(Long externalId);

    Optional<MovieEntity> findByExternalId(Long externalId);

    /**
     * Поиск фильма по идентификатору из внешнего сервиса с вызовом исключения в случае отсутствия.
     * @param externalId идентификатор из внешнего сервиса.
     * @return информация о фильме.
     */
    default MovieEntity getMovieFromExternalIdOrThrow(Long externalId) {
        return findByExternalId(externalId).orElseThrow(() -> new ElementExistException(false));
    }

}
