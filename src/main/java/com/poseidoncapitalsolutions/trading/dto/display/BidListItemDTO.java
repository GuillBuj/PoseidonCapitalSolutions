package com.poseidoncapitalsolutions.trading.dto.display;

public record BidListItemDTO(
    int id,
    String account,
    String type,
    double bidQuantity
) {

}
