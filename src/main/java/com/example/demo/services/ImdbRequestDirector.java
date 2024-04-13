package com.example.demo.services;

import com.example.demo.dto.ExternalServiceRequest;
import com.example.demo.dto.MovieRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "services", name = "movie-service", havingValue = "imdb")
public class ImdbRequestDirector implements ServiceRequestDirector {
    @Override
    public <T extends ExternalServiceRequest> T buildMovieRequest(MovieRequest request) {
        return null;
    }

    @Override
    public <T extends ExternalServiceRequest> T buildRandomSloganRequest() {
        return null;
    }

    @Override
    public <T extends ExternalServiceRequest> T buildRatingRequest(List<Long> ids) {
        return null;
    }

}
