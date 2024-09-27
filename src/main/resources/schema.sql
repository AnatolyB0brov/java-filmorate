DROP TABLE IF EXISTS film_genres CASCADE;
DROP TABLE IF EXISTS genres CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS statuses CASCADE;
DROP TABLE IF EXISTS ratings CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS films CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id       LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name     VARCHAR(255),
    login    VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    birthday DATE         NOT NULL
);

CREATE UNIQUE INDEX IF NOT EXISTS user_email_ind ON users (email);
CREATE UNIQUE INDEX IF NOT EXISTS user_login_ind ON users (login);

CREATE TABLE IF NOT EXISTS films
(
    id          LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    releaseDate DATE         NOT NULL,
    duration    INT,
    rating_id   LONG
);

CREATE TABLE IF NOT EXISTS status
(
    id   LONG GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS friends
(
    PRIMARY KEY (user_id, friend_id),
    user_id   LONG,
    friend_id LONG,
    status_id LONG,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (friend_id) REFERENCES users (id),
    FOREIGN KEY (status_id) REFERENCES status (id)
);

CREATE TABLE IF NOT EXISTS ratings
(
    id          LONG AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS likes
(
    film_id LONG,
    user_id LONG,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS genres
(
    id   LONG AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS film_genres
(
    id       LONG AUTO_INCREMENT,
    film_id  LONG,
    genre_id LONG,
    FOREIGN KEY (film_id) REFERENCES films (id),
    FOREIGN KEY (genre_id) REFERENCES genres (id)
);