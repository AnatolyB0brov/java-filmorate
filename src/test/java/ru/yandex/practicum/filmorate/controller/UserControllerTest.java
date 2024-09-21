package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    static ValidatorFactory validatorFactory;
    static Validator validator;
    User user;

    @BeforeAll
    static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void close() {
        validatorFactory.close();
    }

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1);
        user.setEmail("email@mail.ru");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 10, 5));
    }

    @Test
    void emailIsCorrect() {
        user.setEmail("asdasdsad");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Отправленная строка не соответсвует шаблону email", violations.iterator().next().getMessage());
    }

    @Test
    void emailIsNotBlank() {
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Email не может быть пустым", violations.iterator().next().getMessage());
        user.setEmail("");
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Email не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void loginIsNotBlank() {
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым", violations.iterator().next().getMessage());
        user.setLogin("");
        violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void loginIsCorrect() {
        user.setLogin("Login log");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Логин не может содержать пробелы", violations.iterator().next().getMessage());
    }

    @Test
    void birthdayNotInFuture() {
        user.setBirthday(LocalDate.of(2026, 10, 5));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        assertEquals("Дата рождения не может быть в будущем", violations.iterator().next().getMessage());
    }
}