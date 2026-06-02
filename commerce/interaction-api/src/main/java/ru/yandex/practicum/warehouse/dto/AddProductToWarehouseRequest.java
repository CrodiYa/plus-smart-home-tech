package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AddProductToWarehouseRequest {
    @NotNull
    private UUID productId;
    @Min(1)
    @NotNull
    private int quantity;
}
