package ru.yandex.practicum.cart.exception;

import java.util.UUID;

public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }

    public NoProductsInShoppingCartException(UUID uuid) {
        super("Товара с id " + uuid + " в корзине нет");
    }
}
