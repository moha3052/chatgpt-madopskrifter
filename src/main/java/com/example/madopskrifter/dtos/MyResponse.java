package com.example.madopskrifter.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyResponse {
  private String answer;  // OpenAI's svar (f.eks. opskriften)
  private List<SelectedProduct> selectedProducts;  // Valgte produkter
  private List<Map<String, String>> messages;  // Eventuelle OpenAI beskeder

  public MyResponse(String answer) {
    this.answer = answer;
  }

  public MyResponse(String answer, List<SelectedProduct> selectedProducts) {
    this.answer = answer;
    this.selectedProducts = selectedProducts;
  }
}
