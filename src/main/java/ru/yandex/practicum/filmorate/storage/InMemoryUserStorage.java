package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private long currentId = 0;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User user) {
        validate(user);
        user.setId(++currentId);
        users.put(user.getId(), user);
        log.debug("Создан пользователь с названием " + user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        if (!users.containsKey(id)) {
            log.debug("Не найден пользователь с id = " + id);
            throw new UserNotFoundException("Пользователь не найден");
        }
        validate(user);
        users.put(id, user);
        log.debug("Изменен пользователь с названием " + user.getName());
        return user;
    }

    @Override
    public Optional<User> getUserById(long userId) {
        return Optional.ofNullable(users.get(userId));
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
