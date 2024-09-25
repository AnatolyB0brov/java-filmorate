package ru.yandex.practicum.filmorate.utils;

import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

public class ServicesUtils {
    public static User getUserByIdOrElseThrow(UserStorage userStorage, long userId) throws UserNotFoundException {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + userId + " не найден"));
    }
}
