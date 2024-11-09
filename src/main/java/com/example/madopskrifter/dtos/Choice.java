package com.example.madopskrifter.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Choice {
    private int index;
    private Message message;
    private String finishReason;
}