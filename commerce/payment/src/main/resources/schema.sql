CREATE SCHEMA IF NOT EXISTS payment;

CREATE TABLE IF NOT EXISTS payment (
    payment_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    total_payment NUMERIC(19, 2),
    delivery_total NUMERIC(19, 2),
    product_total NUMERIC(19, 2),
    status VARCHAR(50)
);