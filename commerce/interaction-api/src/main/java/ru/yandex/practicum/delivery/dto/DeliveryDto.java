package ru.yandex.practicum.delivery.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.delivery.enums.DeliveryState;
import ru.yandex.practicum.warehouse.dto.AddressDto;

import java.util.UUID;

@Getter
@Setter
@Builder
public class DeliveryDto {
    private UUID deliveryId;
    private AddressDto fromAddress;
    private AddressDto toAddress;
    private UUID orderId;
    private DeliveryState deliveryState;
}
