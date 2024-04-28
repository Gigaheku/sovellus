package com.harjoitus.sovellus.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import java.util.Collections;
import java.util.Map;

@Service
public class OmdbApiService {
    private final RestTemplate restTemplate;

    @Value("${omdb.api.key}")
    private String apiKey;

    public OmdbApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("movies")
    public Map<String, Object> getMovieDetails(String title) {
        String url = UriComponentsBuilder.fromHttpUrl("http://www.omdbapi.com/")
                .queryParam("apikey", apiKey)
                .queryParam("t", title)
                .toUriString();

        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Log the status code and the error message
            System.out.println("Error fetching movie details: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            // You could return a default error message or handle based on status code
            return Collections.singletonMap("error", "No data found for the provided title.");
        } catch (Exception e) {
            // General error handling, such as for network issues
            System.out.println("An error occurred: " + e.getMessage());
            return Collections.singletonMap("error", "Unable to fetch movie details at this time.");
        }
    }
}

