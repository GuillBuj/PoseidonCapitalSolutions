package com.poseidoncapitalsolutions.trading.dto.display;

public record CurvePointListItemDTO(
    int id,
    int curveId,
    double term,
    double value
) {

}
