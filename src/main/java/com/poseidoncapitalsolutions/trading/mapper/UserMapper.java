package com.poseidoncapitalsolutions.trading.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.poseidoncapitalsolutions.trading.dto.UserCreateDTO;
import com.poseidoncapitalsolutions.trading.dto.UserUpdateDTO;
import com.poseidoncapitalsolutions.trading.dto.display.UserListItemDTO;
import com.poseidoncapitalsolutions.trading.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserCreateDTO dto);
   
    UserUpdateDTO toDTO(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateUserFromDTO(UserUpdateDTO dto, @MappingTarget User entity);
    
    UserListItemDTO toListItemDTO(User entity);

    List<UserListItemDTO> toListItemDTOList(List<User> entities);
}
