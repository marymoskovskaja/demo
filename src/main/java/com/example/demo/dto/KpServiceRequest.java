package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class KpServiceRequest implements ExternalServiceRequest {

    private Integer limit;
    private Integer page;
    private List<Long> id;
    private List<String> selectFields;
    private List<String> notNullFields;
    private Boolean isSeries;
    private String movieLength;
    private String year;
    @JsonProperty("rating.kp")
    private String rating;

    public Map toMap() {
        return new ObjectMapper().convertValue(this, Map.class);
    }

}
