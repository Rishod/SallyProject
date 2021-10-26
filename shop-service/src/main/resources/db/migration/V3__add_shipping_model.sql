create table if not exists shipping (
    id uuid PRIMARY KEY,
    order_id uuid not null,
    customer_id uuid not null,
    customer_name VARCHAR(255) not null,
    status VARCHAR(255) not null,
    shop_id uuid not null,
    created_at timestamp without time zone not null default current_timestamp,
    updated_at timestamp without time zone not null default current_timestamp
);

create table if not exists shipping_item (
    id uuid PRIMARY KEY,
    product_id uuid not null,
    shipping_id uuid not null,
    product_name VARCHAR(255) not null,
    product_count int not null,
    price numeric(19, 2) not null,
    created_at timestamp without time zone not null default current_timestamp,
    updated_at timestamp without time zone not null default current_timestamp,
    CONSTRAINT shipping_item_shipping_fk FOREIGN KEY (shipping_id) REFERENCES shipping(id)
)