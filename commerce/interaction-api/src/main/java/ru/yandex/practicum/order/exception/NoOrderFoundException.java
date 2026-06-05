package ru.yandex.practicum.order.exception;

import java.util.UUID;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }

    public NoOrderFoundException(UUID orderId) {
        super("Order with id " + orderId + " not found");
    }
}
