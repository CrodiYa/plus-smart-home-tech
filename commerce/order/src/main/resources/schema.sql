CREATE SCHEMA IF NOT EXISTS orders;

CREATE TABLE IF NOT EXISTS orders.orders (
    order_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    state VARCHAR(50),
    username VARCHAR(100),
    delivery_weight DECIMAL(10, 6),
    delivery_volume DECIMAL(10, 6),
    fragile BOOLEAN,
    total_price NUMERIC(19, 2),
    delivery_price NUMERIC(19, 2),
    product_price NUMERIC(19, 2)
);

CREATE TABLE IF NOT EXISTS orders.order_products (
    order_id UUID,
    product_id UUID,
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders.orders (order_id)
);