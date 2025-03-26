package com.poseidoncapitalsolutions.trading.dto;

public record RatingAddDTO(
    String moodysRating,
    String sandPRating,
    String fitchRating,
    Integer orderNumber
) {

}
