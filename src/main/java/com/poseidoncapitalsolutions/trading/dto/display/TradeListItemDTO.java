package com.poseidoncapitalsolutions.trading.dto.display;

public record TradeListItemDTO(
    int id,
    String account,
	String type,
	Double buyQuantity
) {

}
