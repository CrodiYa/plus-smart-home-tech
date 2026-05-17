package ru.yandex.practicum.service;

import ru.yandex.practicum.cart.dto.ChangeProductQuantityRequest;
import ru.yandex.practicum.cart.dto.ShoppingCartDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CartService {

    ShoppingCartDto getShoppingCart(String username);

    ShoppingCartDto addProduct(String username, Map<UUID, Integer> products);

    void deactivateCart(String username);

    ShoppingCartDto removeProductsFromCart(String username, List<UUID> productsId);

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request);
}
