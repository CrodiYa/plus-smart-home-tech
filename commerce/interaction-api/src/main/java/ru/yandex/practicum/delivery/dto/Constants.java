package ru.yandex.practicum.delivery.dto;

import java.math.BigDecimal;

public class Constants {
    public static final BigDecimal BASE_DELIVERY_RATE = BigDecimal.valueOf(5);

    public static final String FIRST_ADDRESS = "ADDRESS_1";
    public static final String SECOND_ADDRESS = "ADDRESS_2";
    public static final BigDecimal FIRST_ADDRESS_COEFFICIENT = BigDecimal.ONE;
    public static final BigDecimal SECOND_ADDRESS_COEFFICIENT = BigDecimal.TWO;
    public static final BigDecimal SAME_ADDRESS_COEFFICIENT = BigDecimal.valueOf(0.2);

    public static final BigDecimal FRAGILE_COEFFICIENT = BigDecimal.valueOf(0.2);
    public static final BigDecimal WEIGHT_COEFFICIENT = BigDecimal.valueOf(0.3);
    public static final BigDecimal VOLUME_COEFFICIENT = BigDecimal.valueOf(0.2);
}
