CREATE SCHEMA IF NOT EXISTS warehouse;

CREATE TABLE IF NOT EXISTS warehouse.products (
    product_id UUID PRIMARY KEY,
    fragile BOOLEAN,
    width DECIMAL(10, 6),
    height DECIMAL(10, 6),
    depth DECIMAL(10, 6),
    weight DECIMAL(10, 6),
    quantity INTEGER DEFAULT 0
);