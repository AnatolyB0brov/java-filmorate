SET referential_integrity FALSE;
TRUNCATE TABLE film_genres;
TRUNCATE TABLE genres RESTART IDENTITY;
TRUNCATE TABLE likes;
TRUNCATE TABLE friends;
TRUNCATE TABLE status RESTART IDENTITY;
TRUNCATE TABLE ratings RESTART IDENTITY;
TRUNCATE TABLE users RESTART IDENTITY;
TRUNCATE TABLE films RESTART IDENTITY;
SET referential_integrity TRUE;

INSERT INTO ratings(name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');


INSERT INTO films (name, description, releaseDate, duration, rating_id)
VALUES ('Драматик', 'Драматическое описание...', '1994-07-01', 110, 3),
       ('Комедийка', 'Комедийное описание ...', '1997-05-25', 120, 2),
       ('Триллерок документальный', 'Триллерно-документальное описание ...', '1999-12-06', 190, 4),
       ('Боевичок', 'Боевиковое описание ...', '2010-06-27', 95, 2);

INSERT INTO film_genres (film_id, genre_id)
VALUES (1, 2),
       (2, 1),
       (3, 4),
       (3, 5),
       (4, 6);

INSERT INTO users (name, login, email, birthday)
VALUES ('Ivan', 'IvanSini', 'IvanSinicin123@mail.ru', '1991-05-21'),
       ('Roman', 'RomanYaga', 'RomanEgorov@yandex.ru', '1997-02-15'),
       ('Nikita', 'BigNikita', 'NikitaPetrov@yandex.ru', '2005-09-11');

INSERT INTO likes (film_id, user_id)
VALUES (1, 2),
       (1, 3),
       (2, 1),
       (2, 2),
       (2, 3),
       (3, 2),
       (4, 1);

INSERT INTO status (name)
VALUES ('Подтверждённая'),
       ('Неподтверждённая');

INSERT INTO friends (user_id, friend_id, status_id)
VALUES (1, 2, 1),
       (1, 3, 1),
       (2, 1, 2),
       (2, 3, 1),
       (3, 1, 2),
       (3, 2, 1);