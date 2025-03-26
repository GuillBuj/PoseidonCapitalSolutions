package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.dto.BidUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.BidListItemDTO;
import com.poseidoncapitalsolutions.trading.model.Bid;

@Mapper(componentModel = "spring")
public interface BidMapper {

    Bid toEntity(BidAddDTO dto);
   
    BidUpdateDTO toDTO(Bid entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBidFromDTO(BidUpdateDTO dto, @MappingTarget Bid entity);
    
    BidListItemDTO toListItemDTO(Bid entity);

    List<BidListItemDTO> toListItemDTOList(List<Bid> entities);
}
