package ru.yandex.practicum.warehouse.exception;

import java.util.UUID;

public class NoSpecifiedProductInWarehouseException extends RuntimeException {
    public NoSpecifiedProductInWarehouseException(String message) {
        super(message);
    }

    public NoSpecifiedProductInWarehouseException(UUID productId) {
        super("Товар с id " + productId + " не найден на складе");
    }
}
