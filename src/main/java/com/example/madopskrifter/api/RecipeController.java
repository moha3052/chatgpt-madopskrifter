package com.example.madopskrifter.api;

import com.example.madopskrifter.dtos.MyResponse;
import com.example.madopskrifter.dtos.ClearanceResponseDTO;
import com.example.madopskrifter.service.OpenAiService;
import com.example.madopskrifter.service.SallingGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/recipes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecipeController {

  private final OpenAiService openAiService;
  public final SallingGroupService sallingGroupService;

  @GetMapping("wasteFood")
  public ResponseEntity<ClearanceResponseDTO> getSalling() {
    ClearanceResponseDTO response = sallingGroupService.wasteFood();
    return ResponseEntity.ok(response);
  }

  @GetMapping("generate")
  public ResponseEntity<MyResponse> generateRecipe(@RequestParam List<String> selectedProductNames) {
    if (selectedProductNames.isEmpty()) {
      return ResponseEntity.badRequest().body(new MyResponse("Ingen varer valgt. Vælg venligst varer."));
    }

    try {
      // Generer opskrifter ved at sende de valgte produkter til OpenAI
      MyResponse response = openAiService.generateRecipes(selectedProductNames);
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
              .body(new MyResponse("Der opstod en fejl under genereringen af opskrifter."));
    }
  }





}
