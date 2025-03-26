package com.poseidoncapitalsolutions.trading.mapper;


import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.poseidoncapitalsolutions.trading.dto.RuleNameAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RuleNameUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RuleNameListItemDTO;
import com.poseidoncapitalsolutions.trading.model.RuleName;

@Mapper(componentModel = "spring")
public interface RuleNameMapper {

    RuleName toEntity(RuleNameAddDTO dto);

    RuleNameUpdateDTO toDTO(RuleName entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RuleNameUpdateDTO dto, @MappingTarget RuleName entity);
    
    RuleNameListItemDTO toListItemDTO(RuleName entity);

    List<RuleNameListItemDTO> toListItemDTOList(List<RuleName> entities);
}
