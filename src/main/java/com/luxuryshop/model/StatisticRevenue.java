package com.luxuryshop.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StatisticRevenue {
    private LocalDate date;
    private Integer quantitySold;
    private Float revenue;
}
