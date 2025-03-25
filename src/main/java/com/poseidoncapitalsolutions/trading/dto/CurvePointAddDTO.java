package com.poseidoncapitalsolutions.trading.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CurvePointAddDTO(
    @NotNull(message = "must not be null")
    @Positive
    Integer curveId,
    Double term,
    Double value
) {

}
