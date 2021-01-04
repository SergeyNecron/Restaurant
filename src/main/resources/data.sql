INSERT INTO USERS (EMAIL, PASSWORD)
VALUES ('user@gmail.com','{noop}password'),
       ('admin@javaops.ru','{noop}admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('USER', 1),
       ('ADMIN', 2),
       ('USER', 2);
