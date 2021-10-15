insert into saly_user(id, username, password)
VALUES ('cb6b6a69-ea4e-492c-9491-9bdc90fafed7', 'coffeeLabOwner', '$2a$10$GMGckzvtH/2OK0hYA51QyOCem4nFGFkxw5otkPezyd7hwl2n9.otO'),
       ('8093a41f-3c1f-4268-b983-555cab4947cc', 'aromaCoffeeOwner', '$2a$10$QWtUcK1OtAq9B5NOAboSmOcb2spP1Z/YORkng1130Xkik.cdFfA6a'),
       ('7b45ccb3-30df-4122-8166-8d4d9aeb5878', 'Mark', '$2a$10$g1Rk2mbRnUZg1SpYZ86.VuEAt9F9PO9Rt5MOYYdx7YFBH3d.y/CeG'),
       ('97d3c4a2-256b-4b20-a182-7ebb965475b4', 'Tom', '$2a$10$dBwgnyeg/zPjNX3T0LZmbuyIom9Ofg7hT0fiP8xpxFyEI4xKBuCjm'),
       ('5b868e77-8e8d-4212-819f-54f2dbec392d', 'Lisa', '$2a$10$yHlMbei4Q6lVaQM6E9LFUO2t85zJa2qySLOTb7QCT2k0Yst3NrXhe');

insert into roles(role, user_id)
VALUES ('SHOP_OWNER', 'cb6b6a69-ea4e-492c-9491-9bdc90fafed7'),
       ('SHOP_OWNER', '8093a41f-3c1f-4268-b983-555cab4947cc'),
       ('CUSTOMER', '7b45ccb3-30df-4122-8166-8d4d9aeb5878'),
       ('CUSTOMER', '97d3c4a2-256b-4b20-a182-7ebb965475b4'),
       ('CUSTOMER', '5b868e77-8e8d-4212-819f-54f2dbec392d');

insert into shop(id, name, shop_owner_id)
VALUES ('ba2f8041-6287-4b6d-b9ff-722d752192f1', 'Coffee Lab', 'cb6b6a69-ea4e-492c-9491-9bdc90fafed7'),
       ('8093a41f-3c1f-4268-b983-555cab4947cc', 'Aroma Coffee', '8093a41f-3c1f-4268-b983-555cab4947cc');

