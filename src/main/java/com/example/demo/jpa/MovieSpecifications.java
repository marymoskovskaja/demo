package com.example.demo.jpa;

import com.example.demo.domain.MovieEntity;
import com.example.demo.dto.MovieRequest;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MovieSpecifications {

    public static Specification<MovieEntity> getMovieSearchSpecification(MovieRequest request) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.equal(root.get("isWatched"), false));

            if (Objects.nonNull(request.getMinLength())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("length"), request.getMinLength()));
            }

            if (Objects.nonNull(request.getMaxLength())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("length"), request.getMaxLength()));
            }

            if (Objects.nonNull(request.getMinYear())) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), request.getMinYear()));
            }

            if (Objects.nonNull(request.getMaxYear())) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("year"), request.getMaxYear()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
