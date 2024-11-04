package com.example.madopskrifter.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SallingGroupService {

    private static final Logger logger = LoggerFactory.getLogger(SallingGroupService.class);

    @Value("${app.salling-api-key}")
    private String sallingApiKey;

    @Value("${app.salling-url}")
    private String sallingUrl;

    private final WebClient client;

    public SallingGroupService(WebClient.Builder webClientBuilder) {
        this.client = webClientBuilder.build();
    }

    public String fetchRelevantProductInfo(String query) {
        String url = "https://api.sallinggroup.com/v1-beta/product-suggestions/relevant-products?query="  + query;

        try {
            return client.get()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + sallingApiKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (WebClientResponseException e) {
            logger.error("Salling Group API error: " + e.getResponseBodyAsString());
            throw new ResponseStatusException(
                    HttpStatus.valueOf(e.getRawStatusCode()),
                    "Fejl ved hentning af relevant produktinformation fra Salling Group API"
            );
        }
    }
}
