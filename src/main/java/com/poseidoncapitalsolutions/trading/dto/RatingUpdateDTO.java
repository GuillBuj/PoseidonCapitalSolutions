package com.poseidoncapitalsolutions.trading.dto;

public record RatingUpdateDTO(
    int id,
    String moodysRating,
    String sandPRating,
    String fitchRating,
    Integer orderNumber
) {

}
