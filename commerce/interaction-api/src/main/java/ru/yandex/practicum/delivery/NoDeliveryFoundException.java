package ru.yandex.practicum.delivery;

import java.util.UUID;

public class NoDeliveryFoundException extends RuntimeException {
    public NoDeliveryFoundException(String message) {
        super(message);
    }
    public NoDeliveryFoundException(UUID deliveryId) {
        super("Delivery with id " + deliveryId + " not found");
    }
}
