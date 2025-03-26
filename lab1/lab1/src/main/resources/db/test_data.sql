INSERT INTO users (username, role, password) VALUES
('user1', 'ROLE_USER', '$2a$10$bF.JhIykRbsPg8gxKfWZZu3OMloVeGRmRdS8C8GV.lDnXJlyMoNOq');

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
