package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    static ValidatorFactory validatorFactory;
    static Validator validator;
    Film film;

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
        film = new Film();
        film.setId(1);
        film.setName("Name");
        film.setDescription("Description description description description description description description");
        film.setReleaseDate(LocalDate.of(2022, 10, 5));
        film.setDuration(243);
    }

    @Test
    void nameNotBlank() {
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage());
        film.setName("");
        violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Название не может быть пустым", violations.iterator().next().getMessage());
    }

    @Test
    void releaseNotInFuture() {
        film.setReleaseDate(LocalDate.of(2026, 10, 5));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Релиз не может быть в будущем", violations.iterator().next().getMessage());
    }

    @Test
    void releaseNotBefore28_12_1895() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Дата релиза не должна быть раньше 28.12.1895", violations.iterator().next().getMessage());
    }

    @Test
    void descriptionNotMore200() {
        film.setDescription("Description".repeat(30));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Описание не должно быть более 200 символов", violations.iterator().next().getMessage());
    }

    @Test
    void durationNotLessThan1() {
        film.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertEquals(1, violations.size());
        assertEquals("Продолжительность должна быть больше 0", violations.iterator().next().getMessage());
    }
}