package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cart.controller.CartClient;
import ru.yandex.practicum.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;
import ru.yandex.practicum.service.CartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@RequiredArgsConstructor
public class CartController implements CartClient {

    private final CartService cartService;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        return cartService.getShoppingCart(username);
    }

    @Override
    public ShoppingCartDto addProduct(String username, Map<UUID, Integer> products) {
        return cartService.addProduct(username, products);
    }

    @Override
    public void deactivateCart(String username) {
        cartService.deactivateCart(username);
    }

    @Override
    public ShoppingCartDto removeProductsFromCart(String username, List<UUID> products) {
        return cartService.removeProductsFromCart(username, products);
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        return cartService.changeQuantity(username, request);
    }
}
