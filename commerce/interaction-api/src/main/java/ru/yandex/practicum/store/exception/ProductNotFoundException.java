package ru.yandex.practicum.store.exception;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(UUID productId) {
        super("Товар с id " + productId + " не найден");
    }
}
