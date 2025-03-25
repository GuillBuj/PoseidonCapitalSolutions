package com.poseidoncapitalsolutions.trading.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record RatingUpdateDTO(
    int id,
    String moodysRating,
    String sandPRating,
    String fitchRating,
    Integer orderNumber
) {

}
