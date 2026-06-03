package ru.yandex.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.model.Address;
import ru.yandex.practicum.warehouse.dto.AddressDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AddressMapper {
    AddressDto toAddressDto(Address address);

    Address toAddress(AddressDto addressDto);
}
