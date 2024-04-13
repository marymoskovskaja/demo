package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="movie")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "movie_seq")
    @Column(name = "movie_id", nullable = false, unique = true)
    private Long movieId;

    @Column(name = "movie_name", nullable = false)
    private String movieName;

    @Column(name = "description")
    private String description;

    @Column(name = "year", nullable = false)
    private Long year;

    @Column(name = "country")
    private String country;

    @Column(name = "external_id", nullable = false)
    private Long externalId;

    @Column(name = "length")
    private Long length;

    @Column(name = "is_watched")
    private Boolean isWatched;

}
