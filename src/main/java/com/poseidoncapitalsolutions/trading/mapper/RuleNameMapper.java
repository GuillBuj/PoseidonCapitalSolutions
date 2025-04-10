package com.poseidoncapitalsolutions.trading.mapper;


import java.util.List;

import org.mapstruct.*;

import com.poseidoncapitalsolutions.trading.dto.RuleNameAddDTO;
import com.poseidoncapitalsolutions.trading.dto.RuleNameUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.RuleNameListItemDTO;
import com.poseidoncapitalsolutions.trading.model.RuleName;

@Mapper(componentModel = "spring")
public interface RuleNameMapper {

    @Mapping(target = "id", ignore = true)
    RuleName toEntity(RuleNameAddDTO dto);

    RuleNameUpdateDTO toDTO(RuleName entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(RuleNameUpdateDTO dto, @MappingTarget RuleName entity);
    
    RuleNameListItemDTO toListItemDTO(RuleName entity);

    List<RuleNameListItemDTO> toListItemDTOList(List<RuleName> entities);
}
