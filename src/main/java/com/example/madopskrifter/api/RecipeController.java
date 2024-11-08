package com.example.madopskrifter.api;

import com.example.madopskrifter.dtos.MyResponse;
import com.example.madopskrifter.dtos.ClearanceResponseDTO;
import com.example.madopskrifter.service.OpenAiService;
import com.example.madopskrifter.service.SallingGroupService;
import lombok.RequiredArgsConstructor;
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

  // Endpoint til at generere opskrifter via OpenAI
  @GetMapping("generate")
  public ResponseEntity<MyResponse> getRecipe(@RequestParam List<String> selectedProductNames) {
    System.out.println("Selected product names: " + selectedProductNames);
    if (selectedProductNames.isEmpty()) {
      return ResponseEntity.badRequest().body(new MyResponse("Ingen varer valgt. Vælg venligst varer."));
    }

    MyResponse response = openAiService.generateRecipes(selectedProductNames);
    return ResponseEntity.ok(response);
  }




}
