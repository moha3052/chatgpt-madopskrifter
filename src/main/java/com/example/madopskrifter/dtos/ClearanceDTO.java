package com.example.madopskrifter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClearanceDTO {
    private OfferDTO offer;
    private ProductDTO product;
}
