package com.poseidoncapitalsolutions.trading.dto;


public record RuleNameAddDTO(
    String name,
    String description,
    String json,
    String template,
    String sqlStr,
    String sqlPart
) {

}
