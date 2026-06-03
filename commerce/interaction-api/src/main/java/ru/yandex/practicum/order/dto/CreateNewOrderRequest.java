package ru.yandex.practicum.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.warehouse.dto.AddressDto;

@Getter
@Setter
@Builder
public class CreateNewOrderRequest {

    @NotBlank
    private String username;

    @Valid
    @NotNull
    private ShoppingCartDto shoppingCart;
    @Valid
    @NotNull
    private AddressDto deliveryAddress;
}
