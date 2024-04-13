package com.example.demo.services.cloud;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Objects;

/**
 * Перехватчик запросов к сторонним сервисам, добавляет в header запроса ключ для доступа КП.
 */
@Component
@ConditionalOnProperty(prefix = "services", name = "movie-service", havingValue = "kp")
public class AddApiKeyFeignInterceptor implements RequestInterceptor {

    @Value("${services.kp-address}")
    private String url;

    @Value("${kp-api-key}")
    private String key;

    @Override
    public void apply(RequestTemplate template) {
        if (Objects.nonNull(template.request().url()) && template.feignTarget().url().equals(url)) {
            template.header("X-API-KEY", key);
        }
    }

}