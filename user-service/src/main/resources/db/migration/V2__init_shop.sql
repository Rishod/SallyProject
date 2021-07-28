create table if not exists shop (
     id uuid PRIMARY KEY,
     name VARCHAR(255) not null unique,
     shop_owner_id uuid not null,
     created_at timestamp without time zone not null default current_timestamp,
     updated_at timestamp without time zone not null default current_timestamp,
     CONSTRAINT roles_user_fk1 FOREIGN KEY (shop_owner_id) REFERENCES saly_user(id)
);