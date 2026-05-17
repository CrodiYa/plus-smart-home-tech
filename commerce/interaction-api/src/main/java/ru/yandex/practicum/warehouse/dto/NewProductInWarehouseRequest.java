package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class NewProductInWarehouseRequest {
    @NotNull
    private UUID productId;
    private Boolean fragile;
    @Valid
    @NotNull
    private DimensionDto dimension;
    @DecimalMin("1.0")
    @NotNull
    private double weight;
}
