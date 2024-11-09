package com.example.madopskrifter.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Message {
    private String role;  // Rolle (system, user, assistant)
    private String content;  // Indhold af besked
}