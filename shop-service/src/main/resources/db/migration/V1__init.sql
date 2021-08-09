create table if not exists product (
    id uuid PRIMARY KEY,
    name VARCHAR(255) not null,
    description VARCHAR(255),
    price numeric(19, 2) not null,
    shop_id uuid not null,
    created_at timestamp without time zone not null default current_timestamp,
    updated_at timestamp without time zone not null default current_timestamp
)