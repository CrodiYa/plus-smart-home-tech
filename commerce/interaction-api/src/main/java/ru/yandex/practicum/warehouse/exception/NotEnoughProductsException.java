package ru.yandex.practicum.warehouse.exception;

import java.util.UUID;

public class NotEnoughProductsException extends RuntimeException {
    public NotEnoughProductsException(String message) {
        super(message);
    }

    public NotEnoughProductsException(UUID productId) {
        super("Товара с id " + productId + " не хватает на складе");
    }
}
