--INSERT INTO users (username, role, password) VALUES
--('u', 'ROLE_USER', '$2a$10$//.3D7CjGJfsaJnUk0lcfuPnjJKM/dxwaqbI70M5sBxE7Id4.W5MK');
--
--INSERT INTO users (username, role, password) VALUES
--('c', 'ROLE_COURIER', '$2a$10$//.3D7CjGJfsaJnUk0lcfuPnjJKM/dxwaqbI70M5sBxE7Id4.W5MK');
--
--
--INSERT INTO shopping_cart (user_id) VALUES
--(1);

insert into product_category(title, description) values
    ('смартфоны', 'Мобильные телефоны различных брендов');
insert into product_category(title, description) values
    ('умные часы', 'Умные часы и фитнес-браслеты');
insert into product_category(title, description) values
    ('аксессуары', 'Наушники и зарядки по 100 рублей на андроид и на айфон');

INSERT INTO product (title, description, price, quantity, product_category_id) VALUES
('Xiaomi Redmi Note 14', '256 Гб LTE полуночный черный', 22990.00, 459, 1),
('Samsung Galaxy S23', '128 Гб 5G чёрный', 79990.00, 210, 1),
('iPhone 15 Pro', '256 Гб 5G титановый серый', 124990.00, 185, 1),
('Realme 11 Pro+', '512 Гб LTE солнечное золото', 34990.00, 320, 1),
('OnePlus Nord 3', '256 Гб 5G серый туман', 42990.00, 150, 1),
('Часы HUAWEI Watch D2 Luca', 'белые', 28990.00, 6743, 2),
('Apple Watch Ultra 2', 'титановый корпус', 79990.00, 1250, 2),
('Samsung Galaxy Watch6 Classic', 'черный, 47mm', 34990.00, 2890, 2),
('Xiaomi Watch 2 Pro', 'серебряные', 27990.00, 4321, 2),
('Портативная акустическая система JBL GO 3', 'Blue', 3590.00, 821, 3),
('Переходник Apple с Lightning', '(8-pin) на Jack 3,5mm MMX62ZM/A White', 1490.00, 236, 3),
('Держатель автомобильный RedLine HOL-12', 'гибкая штанга универсальный Black', 449.00, 785, 3);

insert into characteristic_type (description, title, type) values
    ('Цвета разных товаров', 'Цвет', 'TEXT'),
    ('в гигабайтах', 'Память', 'NUMERIC');

insert into characteristic (numeric_value, type_id, text_value) values
    (null, 1, 'красный'),
    (null, 1, 'зеленый'),
    (null, 1, 'серо-буро-малиновый'),
    (null, 1, 'оранжевый'),
    (256.0, 2, null),
    (64.0, 2, null),
    (512.0, 2, null);

insert into product_category_characteristic_type (characteristic_type_id, product_category_id) values
    (1, 1),
    (1, 3),
    (2, 1),
    (2, 2);

insert into product_characteristics(characteristic_id, product_id) values
    (1, 1),
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (2, 10),
    (3, 11),
    (4, 12),
    (5, 1),
    (5, 2),
    (6, 3),
    (6, 4),
    (7, 5),
    (5, 6),
    (6, 7),
    (7, 8),
    (5, 9);

INSERT INTO promo_code (title, start_date, end_date, percentage, condition)
VALUES
    ('first', TO_TIMESTAMP('25.03.2024', 'DD.MM.YYYY'), TO_TIMESTAMP('15.08.2026', 'DD.MM.YYYY'), 15, 'FIRST_ORDER'),
    ('always', TO_TIMESTAMP('25.03.2024', 'DD.MM.YYYY'), TO_TIMESTAMP('15.08.2026', 'DD.MM.YYYY'), 3, 'ALWAYS');