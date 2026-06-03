package ru.yandex.practicum.payment.exception;

import java.util.UUID;

public class PaymentNotFoundException extends RuntimeException {
    public PaymentNotFoundException(String message) {
        super(message);
    }

    public PaymentNotFoundException(UUID paymentId) {
        super("Payment with id " + paymentId + " not found");
    }

}
