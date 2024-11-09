package com.example.madopskrifter.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedProduct {
    private String description;  // Produktbeskrivelse
    private String ean;  // EAN-kode
    private Double newPrice;  // Ny pris
    private Double discount;  // Rabat
    private String stockUnit;  // Enhed for lager
    private String imageUrl;  // Billede af produktet

    public SelectedProduct(String description, String ean, Double newPrice, Double discount, String stockUnit, String imageUrl) {
        this.description = description;
        this.ean = ean;
        this.newPrice = newPrice;
        this.discount = discount;
        this.stockUnit = stockUnit;
        this.imageUrl = imageUrl;
    }
}
