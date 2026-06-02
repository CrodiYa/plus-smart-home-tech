package ru.yandex.practicum.cart.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface CartClient {

    @GetMapping
    ShoppingCartDto getShoppingCart(@RequestParam @NotNull String username);

    @PutMapping
    ShoppingCartDto addProduct(@RequestParam @NotNull String username, @RequestBody Map<UUID, Integer> products);

    @DeleteMapping
    void deactivateCart(@RequestParam @NotNull String username);

    @PostMapping("/remove")
    ShoppingCartDto removeProductsFromCart(@RequestParam @NotNull String username,
                                           @RequestBody List<UUID> productsId);

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(@RequestParam @NotNull String username,
                                   @RequestBody ChangeProductQuantityRequest request);
}
