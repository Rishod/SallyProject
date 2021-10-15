create table if not exists saly_user (
    id uuid PRIMARY KEY,
    username VARCHAR(255) unique not null,
    password VARCHAR(255) not null,
    created_at timestamp without time zone not null default current_timestamp,
    updated_at timestamp without time zone not null default current_timestamp
);

create table if not exists roles (
    role VARCHAR(255) not null,
    user_id uuid not null,
    CONSTRAINT roles_user_fk1 FOREIGN KEY (user_id) REFERENCES saly_user(id)
);
