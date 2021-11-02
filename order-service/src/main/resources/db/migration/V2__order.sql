create table if not exists orders (
    id uuid PRIMARY KEY,
    customer_id uuid not null,
    status VARCHAR(255) not null,
    total numeric(19, 2),
    created_at timestamp without time zone not null default current_timestamp,
    updated_at timestamp without time zone not null default current_timestamp
);

create table if not exists order_item (
     id uuid PRIMARY KEY,
     product_id uuid not null,
     order_id uuid not null,
     product_name VARCHAR(255),
     product_count int not null,
     price numeric(19, 2),
     created_at timestamp without time zone not null default current_timestamp,
     updated_at timestamp without time zone not null default current_timestamp,
     CONSTRAINT order_item_order_fk FOREIGN KEY (order_id) REFERENCES orders(id)
)