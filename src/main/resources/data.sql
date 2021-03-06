INSERT INTO USERS (NAME, EMAIL, PASSWORD)
VALUES ('admin', 'admin@ya.ru', '{noop}admin'),
       ('user', 'user@ya.ru', '{noop}user');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 1),
       ('USER', 2);

INSERT INTO RESTAURANT (NAME, ID)
VALUES ('София', 1),
       ('Макдак', 2),
       ('Пицерия', 3),
       ('Закусочная', 4);

INSERT INTO MENU (ID, NAME, DATE, RESTAURANT_ID)
VALUES (1, 'Меню дня', CURRENT_DATE, 1),
       (2, 'Меню дня', CURRENT_DATE, 2),
       (3, 'Меню дня', CURRENT_DATE, 3),
       (4, 'Меню дня', CURRENT_DATE, 4);

INSERT INTO MEAL (ID, NAME, PRICE, MENU_ID)
VALUES (1, 'Бизнес-ланч', 100.0, 1),
       (2, 'Тёплые ролы', 99.0, 1),
       (3, 'Салат Цезарь', 99.0, 1),
       (4, 'Лимонад', 90.0, 1),
       (5, 'Пицца', 58.9, 1),
       (6, 'Мороженное', 8.0, 1),

       (7, 'Суп', 50.0, 2),
       (8, 'Отбивная', 20.0, 2),
       (9, 'Картофельное пюре', 80.0, 2),
       (10, 'Амлет', 70.0, 2),
       (11, 'Хачапури', 50.0, 2),
       (12, 'Чай', 50.0, 2),

       (13, 'Ланч', 200.0, 3),
       (14, 'Ролы', 99.0, 3),
       (15, 'Салат фруктовый', 99.0, 3),
       (16, 'Кофе', 60.0, 3),
       (17, 'Бургер', 70.0, 3),
       (18, 'Гамбургер', 60.0, 3),

       (19, 'Салат Цезарь', 80.0, 4),
       (20, 'Спагетти с соусом', 50.0, 4),
       (21, 'Щи', 40.0, 4),
       (22, 'Ножки индейки', 500.0, 4),
       (23, 'Горячий шоколад', 80.0, 4),
       (24, 'Кофе', 50.5, 4);