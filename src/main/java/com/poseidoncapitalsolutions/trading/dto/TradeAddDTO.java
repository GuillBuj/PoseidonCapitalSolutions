package com.poseidoncapitalsolutions.trading.dto;

public record TradeAddDTO(
    String account,
	String type,
	Double buyQuantity
) {

}
