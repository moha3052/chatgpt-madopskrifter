package com.example.madopskrifter.api;

import com.example.madopskrifter.dtos.MyResponse;
import com.example.madopskrifter.service.OpenAiService;
import com.example.madopskrifter.service.SallingGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * This class handles fetching a joke via the ChatGPT API
 */
@RestController
@RequiredArgsConstructor// Gør klassen til en REST-controller, som håndterer HTTP-requests og returnerer JSON-respons.
@RequestMapping("/api/recipes") // Angiver en fælles rod-URL for alle endpoints i denne controller.
public class RecipeController {

  private final SallingGroupService sallingGroupService;
  private final OpenAiService openAiService;

  /**
   * Henter produktinformation fra Salling Group API'et og genererer en opskrift baseret på produktet.
   *
   * @param productId ID for det produkt, der skal bruges til at generere opskriften.
   * @return ResponseEntity med enten den genererede opskrift eller en fejlbesked.
   */
  @GetMapping("/{productId}") // Definerer en GET-endpoint, som tager et produkt-ID som path-parameter.
  public ResponseEntity<MyResponse> getRecipe(@PathVariable String productId) {
    try {
      // 1. Hent produktinformation fra Salling Group API'et ved hjælp af produkt-ID'et.
      String productInfo = sallingGroupService.fetchRelevantProductInfo(productId);

      // 2. Brug produktinformationen til at generere en opskrift via ChatGPT API'et.
      MyResponse recipeResponse = openAiService.generateRecipeBasedOnProduct(productInfo);

      // 3. Returner den genererede opskrift med en statuskode 200 OK.
      return ResponseEntity.status(HttpStatus.OK).body(recipeResponse);
    } catch (Exception e) {
      // 4. Hvis der opstår en fejl, returneres en fejlmeddelelse og statuskode 500 (Internal Server Error).
      return new ResponseEntity<>(new MyResponse("Fejl ved generering af opskrift: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}

