package ru.yandex.practicum.filmorate.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private long currentId = 0;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        film.setId(++currentId);
        films.put(film.getId(), film);
        log.info("Создан фильм с названием " + film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        if (!films.containsKey(id)) {
            throw new FilmNotFoundException("Фильм не найден");
        }
        films.put(id, film);
        log.info("Изменен фильм с названием " + film.getName());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        return Optional.ofNullable(films.get(filmId));
    }
}
