package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.utils.ServicesUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
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
        List<User> firstUserCommonFriends = getUserFriends(firstUserId);
        List<User> secondUserCommonFriends = getUserFriends(secondUserId);
        firstUserCommonFriends.retainAll(secondUserCommonFriends);
        return firstUserCommonFriends;
    }

    public User getUserById(long userId) {
        return ServicesUtils.getUserByIdOrElseThrow(userStorage, userId);
    }

    public List<User> getUserFriends(long userId) {
        User user = getUserById(userId);
        List<User> friends = new ArrayList<>();
        Set<Long> userFriendsIds = user.getFriends();
        if (!CollectionUtils.isEmpty(userFriendsIds)) {
            for (long userFriendId : userFriendsIds) {
                friends.add(getUserById(userFriendId));
            }
        }
        return friends;
    }
}
