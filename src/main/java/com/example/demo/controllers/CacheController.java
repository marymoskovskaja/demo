package com.example.demo.controllers;

import com.example.demo.services.CacheService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
@AllArgsConstructor
public class CacheController {

    private final CacheService cacheService;

    @DeleteMapping(value = "/clear")
    @ApiOperation(value = "Сброс кэша вручную")
    public void clearCache() {
        cacheService.clearCache();
    }

}
