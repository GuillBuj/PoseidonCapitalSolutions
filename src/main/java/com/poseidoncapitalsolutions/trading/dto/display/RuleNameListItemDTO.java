package com.poseidoncapitalsolutions.trading.dto.display;


public record RuleNameListItemDTO(
    int id,
    String name,
    String description,
    String json,
    String template,
    String sqlStr,
    String sqlPart
) {

}
