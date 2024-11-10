package com.example.madopskrifter.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenAIResponseDTO {
    private String generatedText;
}
