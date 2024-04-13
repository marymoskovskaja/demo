package com.example.demo.services;

import com.example.demo.dto.ExternalServiceRequest;
import com.example.demo.dto.MovieRequest;

import java.util.List;

/**
 * Общий интерфейс классов, управляющих формированием запросов к внешним клиентам.
 */
public interface ServiceRequestDirector {
    <T extends ExternalServiceRequest> T buildMovieRequest(MovieRequest request);

    <T extends ExternalServiceRequest> T buildRandomSloganRequest();

    <T extends ExternalServiceRequest> T buildRatingRequest(List<Long> ids);

}
