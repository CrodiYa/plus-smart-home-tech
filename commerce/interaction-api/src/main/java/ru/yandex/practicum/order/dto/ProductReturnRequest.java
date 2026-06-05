package ru.yandex.practicum.order.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductReturnRequest {
    private UUID orderId;
    private Map<UUID, Integer> products;
}
