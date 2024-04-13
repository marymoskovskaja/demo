package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Dto цитаты.
 */
@Data
@Builder
public class Quote implements Serializable {

    /**
     * Текст цитаты.
     */
    private String quoteText;
    /**
     * Источник цитаты.
     */
    private String source;
    /**
     * Автор цитаты.
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String quoteAuthor;

}
