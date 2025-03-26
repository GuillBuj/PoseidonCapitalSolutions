package com.poseidoncapitalsolutions.trading.dto;

public record TradeUpdateDTO(
	int id,
    String account,
	String type,
	Double buyQuantity
) {

}
