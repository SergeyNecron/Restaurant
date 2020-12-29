INSERT INTO USERS (EMAIL, PASSWORD)
VALUES ('user1@gmail.com','password'),
       ('admin@javaops.ru','admin');

INSERT INTO USER_ROLE (ROLE, USER_ID)
VALUES ('ROLE_USER', 1),
       ('ROLE_ADMIN', 2),
       ('ROLE_USER', 2);
