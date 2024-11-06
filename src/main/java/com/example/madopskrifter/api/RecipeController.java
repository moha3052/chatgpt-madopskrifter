package com.example.madopskrifter.api;

import com.example.madopskrifter.dtos.MyResponse;
import com.example.madopskrifter.service.OpenAiService;
import com.example.madopskrifter.service.SallingGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecipeController {

  private final OpenAiService openAiService;
  private final SallingGroupService sallingGroupService;

  // Endpoint til at generere opskrifter via OpenAI
  @GetMapping("/relevant")
  public ResponseEntity<MyResponse> getRecipe(@RequestParam String query) {
    MyResponse response = openAiService.makeRequest(query);
    return ResponseEntity.ok(response);
  }
}
