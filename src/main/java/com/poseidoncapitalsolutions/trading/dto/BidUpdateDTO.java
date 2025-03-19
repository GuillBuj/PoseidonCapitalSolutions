package com.poseidoncapitalsolutions.trading.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BidUpdateDTO(
    @NotNull
    int id,
    
    @NotBlank(message = "Account is mandatory")
    String account,

    @NotBlank(message = "Type is mandatory")
    String type,

    @Positive
    double quantity
) {

}
