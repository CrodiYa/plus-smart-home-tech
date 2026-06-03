package ru.yandex.practicum.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yandex.practicum.delivery.dto.DeliveryDto;
import ru.yandex.practicum.model.Delivery;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface DeliveryMapper {

    DeliveryDto toDeliveryDto(Delivery delivery);

    @Mapping(target = "deliveryId", ignore = true)
    @Mapping(target = "deliveryWeight", ignore = true)
    @Mapping(target = "deliveryVolume", ignore = true)
    @Mapping(target = "fragile", ignore = true)
    Delivery toDelivery(DeliveryDto deliveryDto);
}
