package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.*;

import com.poseidoncapitalsolutions.trading.dto.RatingAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RatingUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RatingListItemDTO;
import com.poseidoncapitalsolutions.trading.model.Rating;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    @Mapping(target = "id", ignore = true)
    Rating toEntity(RatingAddDTO dto);

    RatingUpdateDTO toDTO(Rating entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RatingUpdateDTO dto, @MappingTarget Rating entity);
    
    RatingListItemDTO toListItemDTO(Rating entity);

    List<RatingListItemDTO> toListItemDTOList(List<Rating> entities);
}
