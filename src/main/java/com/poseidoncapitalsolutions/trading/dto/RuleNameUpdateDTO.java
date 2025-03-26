package com.poseidoncapitalsolutions.trading.dto;


public record RuleNameUpdateDTO(
    int id,
    String name,
    String description,
    String json,
    String template,
    String sqlStr,
    String sqlPart
) {

}
