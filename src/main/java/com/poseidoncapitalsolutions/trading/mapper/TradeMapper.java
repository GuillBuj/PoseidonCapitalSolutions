package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.poseidoncapitalsolutions.trading.dto.TradeAddDTO;
import com.poseidoncapitalsolutions.trading.dto.TradeUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.TradeListItemDTO;
import com.poseidoncapitalsolutions.trading.model.Trade;

@Mapper(componentModel = "spring")
public interface TradeMapper {

    Trade toEntity(TradeAddDTO dto);
   
    TradeUpdateDTO toDTO(Trade entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTradeFromDTO(TradeUpdateDTO dto, @MappingTarget Trade entity);
    
    TradeListItemDTO toListItemDTO(Trade entity);

    List<TradeListItemDTO> toListItemDTOList(List<Trade> entities);
}
