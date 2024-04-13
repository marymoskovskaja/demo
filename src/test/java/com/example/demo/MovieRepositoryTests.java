package com.example.demo;

import com.example.demo.domain.MovieEntity;
import com.example.demo.dto.MovieRequest;
import com.example.demo.jpa.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MovieRepositoryTests {

    MovieEntity entity = MovieEntity.builder().externalId(10000001L).movieName("Infinite Jest").length(10080L)
            .description("Directed by David Foster Wallace").year(1996L).country("USA").isWatched(false)
            .build();

    @Autowired
    MovieRepository repository;

    @Test
    void saveSuccessfulTest() {
        Assertions.assertDoesNotThrow(() -> repository.save(entity));
    }

    @Test
    void existsByExternalIdExistsTest() {
        repository.save(entity);

        Assertions.assertTrue(repository.existsByExternalId(entity.getExternalId()));
    }

    @Test
    void existsByExternalIdNotExistsTest() {
        repository.save(entity);

        Assertions.assertFalse(repository.existsByExternalId(-1L));
    }

    @Test
    void findAllWithPagingWorkTest() {
        var anotherEntity = MovieEntity.builder().externalId(10000002L).movieName("Titanic 2").length(120L)
                .description("Jack's back").year(2000L).country("USA").isWatched(false).build();

        repository.save(entity);

        Assertions.assertEquals(1, repository.findAllWithPaging(
                MovieRequest.builder().recordCount(1).build()).size());

        repository.save(anotherEntity);

        Assertions.assertEquals(2, repository.findAllWithPaging(
                MovieRequest.builder().recordCount(2).build()).size());
    }

}
