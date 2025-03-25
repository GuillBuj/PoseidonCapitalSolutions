package com.poseidoncapitalsolutions.trading.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record RatingAddDTO(
    String moodysRating,
    String sandPRating,
    String fitchRating,
    Integer orderNumber
) {

}
