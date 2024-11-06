package com.example.madopskrifter.service;

import com.example.madopskrifter.dtos.ChatCompletionRequest;
import com.example.madopskrifter.dtos.ChatCompletionResponse;
import com.example.madopskrifter.dtos.MyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class OpenAiService {

  private static final Logger logger = LoggerFactory.getLogger(OpenAiService.class);

  @Value("${app.api-key}")
  private String API_KEY;

  @Value("${app.url}")
  private String URL;

  @Value("${app.model}")
  private String MODEL;

  @Value("${app.temperature}")
  public double TEMPERATURE;

  @Value("${app.max_tokens}")
  public int MAX_TOKENS;

  @Value("${app.frequency_penalty}")
  public double FREQUENCY_PENALTY;

  @Value("${app.presence_penalty}")
  public double PRESENCE_PENALTY;

  @Value("${app.top_p}")
  public double TOP_P;

  private final WebClient client;
  private final SallingGroupService sallingGroupService;

  public OpenAiService(WebClient.Builder webClientBuilder, SallingGroupService sallingGroupService) {
    this.client = webClientBuilder.build();
    this.sallingGroupService = sallingGroupService;
  }

  public MyResponse makeRequest(String userPrompt) {
    // Fetch relevant products from Salling Group API
    String productData = sallingGroupService.getRelevantProducts(userPrompt);

    String systemMessage = "Alt skal være på dansk"+"You are a helpful cooking assistant. Use the ingredients provided: " + productData +
            "Jeg vil gerne have en historie bag maden, efter liste over ingredienser, efter det step-by-step guide, en total pris. hvor hende de forskellige vare købes";

    ChatCompletionRequest requestDto = new ChatCompletionRequest();
    requestDto.setModel(MODEL);
    requestDto.setTemperature(TEMPERATURE);
    requestDto.setMax_tokens(MAX_TOKENS);
    requestDto.setTop_p(TOP_P);
    requestDto.setFrequency_penalty(FREQUENCY_PENALTY);
    requestDto.setPresence_penalty(PRESENCE_PENALTY);
    requestDto.getMessages().add(new ChatCompletionRequest.Message("system", systemMessage));
    requestDto.getMessages().add(new ChatCompletionRequest.Message("user", userPrompt));

    try {
      ChatCompletionResponse response = client.post()
              .uri(new URI(URL))
              .header("Authorization", "Bearer " + API_KEY)
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .body(BodyInserters.fromValue(requestDto))
              .retrieve()
              .bodyToMono(ChatCompletionResponse.class)
              .block();

      String responseMsg = response.getChoices().get(0).getMessage().getContent();
      return new MyResponse(responseMsg);
    } catch (Exception e) {
      logger.error("Unexpected error: ", e);
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while generating the response.");
    }
  }
}
