package ru.yandex.practicum.payment.exception;

public class NotEnoughInfoInOrderToCalculateException extends RuntimeException {
    public NotEnoughInfoInOrderToCalculateException(String message) {
        super(message);
    }
    public NotEnoughInfoInOrderToCalculateException() {
        super("Not enough info in order to calculate");
    }
}
