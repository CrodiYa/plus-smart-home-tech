package ru.yandex.practicum.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "bookings", schema = "warehouse")
@NoArgsConstructor
public class OrderBooking {
    @Id
    private UUID orderId;
    private UUID deliveryId;
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "bookings_items", schema = "warehouse", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Integer> products;
}
