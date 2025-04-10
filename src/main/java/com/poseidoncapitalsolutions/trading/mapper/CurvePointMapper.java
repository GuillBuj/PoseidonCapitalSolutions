package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.poseidoncapitalsolutions.trading.dto.CurvePointAddDTO;
import com.poseidoncapitalsolutions.trading.dto.CurvePointUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.CurvePointListItemDTO;
import com.poseidoncapitalsolutions.trading.model.CurvePoint;

@Mapper(componentModel = "spring")
public interface CurvePointMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "asOfDate", ignore = true)
    CurvePoint toEntity(CurvePointAddDTO dto);

    CurvePointUpdateDTO toDTO(CurvePoint entity);

    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "asOfDate", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CurvePointUpdateDTO dto, @MappingTarget CurvePoint entity);
    
    @Mapping(source = "id", target = "id")
    @Mapping(source = "curveId", target = "curveId")
    CurvePointListItemDTO toListItemDto(CurvePoint entity);

    List<CurvePointListItemDTO> toListItemDtoList(List<CurvePoint> entities);
}
