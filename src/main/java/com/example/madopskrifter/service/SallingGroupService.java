package com.example.madopskrifter.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SallingGroupService {
    private static final Logger logger = LoggerFactory.getLogger(SallingGroupService.class);

    @Value("${app.salling-api-key}")
    private String sallingApiKey;

    @Value("${app.salling-url}")
    private String sallingUrl;  // Correctly formatted base URL

    private final WebClient client;

    public SallingGroupService(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder
                .baseUrl(sallingUrl)  // Ensure this uses HTTPS and is correct
                .build();
    }


    public String getRelevantProducts(String query) {
        try {
            return this.client.get()
                    .uri("https://api.sallinggroup.com/v1-beta/product-suggestions/relevant-products?query={query}", query)
                    .header("Authorization", "Bearer " + sallingApiKey)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("API call error: " + e.getResponseBodyAsString());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error with the request");
        }
    }
}