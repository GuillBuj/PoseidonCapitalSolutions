package com.poseidoncapitalsolutions.trading.dto.display;


public record RatingListItemDTO(
    int id,
    String moodysRating,
    String sandPRating,
    String fitchRating,
    Integer orderNumber
) {

}
