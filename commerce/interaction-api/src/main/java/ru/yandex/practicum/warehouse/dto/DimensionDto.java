package ru.yandex.practicum.warehouse.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DimensionDto {
    @DecimalMin("1.0")
    private double width;

    @DecimalMin("1.0")
    private double height;

    @DecimalMin("1.0")
    private double depth;
}
