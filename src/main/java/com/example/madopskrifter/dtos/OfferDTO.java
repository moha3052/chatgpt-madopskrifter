package com.example.madopskrifter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OfferDTO {

    private String currency;
    private double discount;
    private LocalDateTime endTime;
    private LocalDateTime lastUpdate;
    private double newPrice;
    private double originalPrice;
    private double percentDiscount;
    private LocalDateTime startTime;
    private double stock;
    private String stockUnit;
}
