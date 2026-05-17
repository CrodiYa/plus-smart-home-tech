package ru.yandex.practicum.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Builder
public class ChangeProductQuantityRequest {
    @NotNull
    private UUID productId;
    @NotNull
    @Positive
    private int newQuantity;
}
