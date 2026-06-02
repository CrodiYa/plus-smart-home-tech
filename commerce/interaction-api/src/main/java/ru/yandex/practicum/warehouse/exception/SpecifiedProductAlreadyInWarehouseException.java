package ru.yandex.practicum.warehouse.exception;

import java.util.UUID;

public class SpecifiedProductAlreadyInWarehouseException extends RuntimeException {
    public SpecifiedProductAlreadyInWarehouseException(String message) {
        super(message);
    }

    public SpecifiedProductAlreadyInWarehouseException(UUID uuid) {
        super("Продукт с таким id " + uuid + " уже существует");
    }
}
