package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.*;

import com.poseidoncapitalsolutions.trading.dto.TradeAddDTO;
import com.poseidoncapitalsolutions.trading.dto.TradeUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.TradeListItemDTO;
import com.poseidoncapitalsolutions.trading.model.Trade;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    @Mapping(target = "trader", ignore = true)
    @Mapping(target = "tradeDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sourceListId", ignore = true)
    @Mapping(target = "side", ignore = true)
    @Mapping(target = "sellQuantity", ignore = true)
    @Mapping(target = "sellPrice", ignore = true)
    @Mapping(target = "security", ignore = true)
    @Mapping(target = "revisionName", ignore = true)
    @Mapping(target = "revisionDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealType", ignore = true)
    @Mapping(target = "dealName", ignore = true)
    @Mapping(target = "creationName", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "buyPrice", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "benchmark", ignore = true)
    Trade toEntity(TradeAddDTO dto);
   
    TradeUpdateDTO toDTO(Trade entity);

    @Mapping(target = "trader", ignore = true)
    @Mapping(target = "tradeDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sourceListId", ignore = true)
    @Mapping(target = "side", ignore = true)
    @Mapping(target = "sellQuantity", ignore = true)
    @Mapping(target = "sellPrice", ignore = true)
    @Mapping(target = "security", ignore = true)
    @Mapping(target = "revisionName", ignore = true)
    @Mapping(target = "revisionDate", ignore = true)
    @Mapping(target = "dealType", ignore = true)
    @Mapping(target = "dealName", ignore = true)
    @Mapping(target = "creationName", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "buyPrice", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "benchmark", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTradeFromDTO(TradeUpdateDTO dto, @MappingTarget Trade entity);
    
    TradeListItemDTO toListItemDTO(Trade entity);

    List<TradeListItemDTO> toListItemDTOList(List<Trade> entities);
}
