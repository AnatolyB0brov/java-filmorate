package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void setLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);
}
