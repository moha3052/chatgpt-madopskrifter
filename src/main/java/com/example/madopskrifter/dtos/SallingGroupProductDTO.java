package com.example.madopskrifter.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SallingGroupProductDTO {
    private String name;
    private double newPrice;
    private double originalPrice;
    private double discount;
}
