# java-filmorate
Template repository for Filmorate project.

## Схема базы данных

![Схема базы данных проекта Filmorate](/images/Diagram.jpg)

### films
- Информация о фильмах

### ratings
- Информация о рейтингах фильмов

### genres
- Информация о жанрах фильмов

### film_genres
- Соединительная таблица для фильмов и их жанров

### users
- Информация о пользователях приложения

### likes
- Соединительная таблица для фильмов и пользователях оценивших фильм

### friends
- Соединительная таблица для пользователей и друзей пользователей. Друг пользователя также является пользователем

### status
- Информация о статусе "дружбы" между пользователями

***

## Примеры запросов на языке SQL для модели User

### 1. Получить информацию о пользователе по его id

#### getUserById(Long userId)
```sql
SELECT *
FROM users AS u
WHERE u.id = {userId};
```

### 2. Добавить друга

#### addFriend(Long userId, Long friendId)
```sql
INSERT INTO friends (user_id, friend_id, status_id)
SELECT {userId}, {friend_id}, s.name FROM status AS s
WHERE s.name = 'Неподтверждённая';
```

### 3. Удалить друга

#### deleteFriend(Long userId, Long friendId)
```sql
DELETE FROM friends
WHERE user_id = {userId} AND friend_id = {friend_id};
```

### 4. Найти всех друзей пользователя

#### getUserFriends(Long userId)
```sql
SELECT u.id, u.name, u.login, u.email, u.birthday 
FROM friends AS f 
INNER JOIN users AS u ON u.id = f.friend_id 
WHERE f.user_id = ? 
ORDER BY u.id
```

### 5. Получить список всех пользователей

#### getUsers()
```sql
SELECT * FROM users
```

### 6. Создать пользователя

#### createUser(User user)
```sql
INSERT INTO users (name, login, email, birthday)
VALUE ({user.getName()}, {user.getLogin()}, {user.getEmail()}, {user.getBirthday()});
```

### 7. Обновить данные пользователя

#### updateUser(User user)
```sql
UPDATE users 
SET name = {user.getName()},
    login = {user.getLogin()},
    email = {user.getEmail()},
    birthday = {user.getBirthday()}
WHERE id = {user.getId()}
```

## Примеры запросов на языке SQL для модели Film

### 1. Поставить лайк фильму

#### setLike(Long filmId, Long userId)
```sql
INSERT INTO likes (film_id, user_id)
VALUE ({filmId}, {userId});
```

### 2. Удалить лайк у фильма

#### deleteLike(Long filmId, Long userId)
```sql
DELETE FROM likes
WHERE film_id = {filmId} AND user_id = {userId};
```

### 3. Получить список N популярных фильмов

#### getMostPopularFilms(Integer count)
```sql
SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id, r.name AS rating_name
FROM films AS f
INNER JOIN ratings AS r ON f.rating_id = r.id
LEFT JOIN likes ON f.id = likes.film_id
GROUP BY f.id
ORDER BY COUNT(likes.film_id) DESC
LIMIT {count};
```

### 4. Получить список всех фильмов

#### getFilms()
```sql
SELECT f.id, f.name, f.description, f.releaseDate, f.duration, f.rating_id, r.name AS rating_name
FROM films AS f
INNER JOIN ratings AS r ON f.rating_id = r.id ORDER BY f.id
```

### 5. Создать запись о фильме

#### createFilm(Film film)
```sql
INSERT INTO films (name, 
                  description, 
                  releaseDate, 
                  duration, 
                  rating)
VALUE ({film.getName()}, 
       {film.getDescription()}, 
       {film.getReleaseDate()}, 
       {film.getDuration()},
       {film.getRating()});
```

### 6. Обновить запись о фильме

#### updateFilm(Film film)
```sql
UPDATE films 
SET name = {film.getName()}, 
    description = {film.getDescription()}, 
    releaseDate = {film.getReleaseDate()}, 
    duration = {film.getDuration()}, 
    rating = {film.getRating()}, 
WHERE id = {film.getId()};
```