package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public UserService(UserStorage userStorage, FriendStorage friendStorage) {
        this.userStorage = userStorage;
        this.friendStorage = friendStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        ifEmptyNameSetLogin(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        User dbUser = getUserById(user.getId());
        if (dbUser == null) {
            throw new UserNotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
        return userStorage.updateUser(user);
    }

    public void addFriend(long firstUserId, long secondUserId) {
        User firstUser = getUserById(firstUserId);//Проверяем наличие пользователя
        User secondUser = getUserById(secondUserId);//Проверяем наличие пользователя
        friendStorage.addFriend(firstUserId, secondUserId);
    }

    public void deleteFriend(long firstUserId, long secondUserId) {
        User firstUser = getUserById(firstUserId);
        User secondUser = getUserById(secondUserId);
        friendStorage.deleteFriend(firstUserId, secondUserId);
        log.info("Удаление друга {} пользователю {}", secondUserId, firstUserId);
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        List<User> firstUserCommonFriends = getUserFriends(firstUserId);
        List<User> secondUserCommonFriends = getUserFriends(secondUserId);
        firstUserCommonFriends.retainAll(secondUserCommonFriends);
        return firstUserCommonFriends;
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    public List<User> getUserFriends(long userId) {
        User user = getUserById(userId);//Проверяем наличие пользователя
        return friendStorage.getUserFriends(userId);
    }

    private void ifEmptyNameSetLogin(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
