package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AssemblyProductsForOrderRequest {

    @NotNull
    private Map<UUID, Integer> products;

    @NotNull
    private UUID orderId;
}
