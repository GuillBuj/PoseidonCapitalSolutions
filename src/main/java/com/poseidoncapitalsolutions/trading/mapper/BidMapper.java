package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.*;

import com.poseidoncapitalsolutions.trading.dto.BidAddDTO;
import com.poseidoncapitalsolutions.trading.dto.BidUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.BidListItemDTO;
import com.poseidoncapitalsolutions.trading.model.Bid;

@Mapper(componentModel = "spring")
public interface BidMapper {

    @Mapping(target = "trader", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sourceListId", ignore = true)
    @Mapping(target = "side", ignore = true)
    @Mapping(target = "security", ignore = true)
    @Mapping(target = "revisionName", ignore = true)
    @Mapping(target = "revisionDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dealType", ignore = true)
    @Mapping(target = "dealName", ignore = true)
    @Mapping(target = "creationName", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "commentary", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "bidListDate", ignore = true)
    @Mapping(target = "bid", ignore = true)
    @Mapping(target = "benchmark", ignore = true)
    @Mapping(target = "askQuantity", ignore = true)
    @Mapping(target = "ask", ignore = true)
    Bid toEntity(BidAddDTO dto);
   
    BidUpdateDTO toDTO(Bid entity);

    @Mapping(target = "trader", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "sourceListId", ignore = true)
    @Mapping(target = "side", ignore = true)
    @Mapping(target = "security", ignore = true)
    @Mapping(target = "revisionName", ignore = true)
    @Mapping(target = "revisionDate", ignore = true)
    @Mapping(target = "dealType", ignore = true)
    @Mapping(target = "dealName", ignore = true)
    @Mapping(target = "creationName", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "commentary", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "bidListDate", ignore = true)
    @Mapping(target = "bid", ignore = true)
    @Mapping(target = "benchmark", ignore = true)
    @Mapping(target = "askQuantity", ignore = true)
    @Mapping(target = "ask", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateBidFromDTO(BidUpdateDTO dto, @MappingTarget Bid entity);
    
    BidListItemDTO toListItemDTO(Bid entity);

    List<BidListItemDTO> toListItemDTOList(List<Bid> entities);
}
