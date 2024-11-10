package com.example.madopskrifter.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
public class DTO {
    private String currency;
    private double discount;
    private String ean;
    private String lastUpdate;
    private double newPrice;
    private double originalPrice;
    private double percentDiscount;
    private double stock;
    private String stockUnit;
    private String da;
    private String en;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
