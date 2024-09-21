package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(long firstUserId, long secondUserId) {
        User firstUser = getUserById(firstUserId);
        User secondUser = getUserById(secondUserId);
        Set<Long> firstUsersFriendsId = firstUser.getFriends();
        Set<Long> secondUsersFriendsId = secondUser.getFriends();
        firstUsersFriendsId.add(secondUser.getId());
        secondUsersFriendsId.add(firstUser.getId());
        log.info("Добавление друга {} пользователю {}", secondUserId, firstUserId);
    }

    public void deleteFriend(long firstUserId, long secondUserId) {
        User firstUser = getUserById(firstUserId);
        User secondUser = getUserById(secondUserId);
        Set<Long> firstUsersFriendsId = firstUser.getFriends();
        Set<Long> secondUsersFriendsId = secondUser.getFriends();
        if (firstUsersFriendsId.contains(secondUser.getId())) {
            firstUsersFriendsId.remove(secondUser.getId());
            secondUsersFriendsId.remove(firstUser.getId());
            log.info("Удаление друга {} пользователю {}", secondUserId, firstUserId);
        }
    }

    public List<User> getCommonFriends(long firstUserId, long secondUserId) {
        log.info("Поиск общих друзей пользователя {} и пользователя {}", secondUserId, firstUserId);
        User firstUser = getUserById(firstUserId);
        User secondUser = getUserById(secondUserId);
        List<User> commonFriends = new ArrayList<>();
        for (Long firstUserFriendId : firstUser.getFriends()) {
            for (Long secondUserFriendId : secondUser.getFriends())
                if (Objects.equals(firstUserFriendId, secondUserFriendId)) {
                    try {
                        commonFriends.add(getUserById(firstUserFriendId));
                    } catch (UserNotFoundException e) {
                        log.debug("Пользователь с id = {} не найден", firstUserFriendId);
                    }
                }
        }
        return commonFriends;
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id = " + userId + " не найден"));
    }

    public List<User> getUserFriends(long userId) {
        User user = getUserById(userId);
        List<User> friends = new ArrayList<>();
        Set<Long> userFriendsIds = user.getFriends();
        if (!CollectionUtils.isEmpty(userFriendsIds)) {
            for (long userFriendId : userFriendsIds) {
                try {
                    friends.add(getUserById(userFriendId));
                } catch (UserNotFoundException e) {
                    log.debug("Пользователь с id = {} не найден", userFriendId);
                }
            }
        }
        return friends;
    }
}
