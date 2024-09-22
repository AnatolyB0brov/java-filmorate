package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;

    private final UserService userService;

    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public void setLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        Set<Long> likes = film.getLikes();
        if (!likes.contains(user.getId())) {
            likes.add(user.getId());
            film.setLikes(likes);
            log.info("Пользователь {} поставил лайк фильму {}", filmId, userId);
            filmStorage.updateFilm(film);
        }
    }

    public void deleteLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        User user = userService.getUserById(userId);
        Set<Long> likes = film.getLikes();
        if (likes.remove(user.getId())) {
            film.setLikes(likes);
            log.info("Пользователь {} убрал лайк фильму {}", userId, filmId);
            filmStorage.updateFilm(film);
        }
    }

    public List<Film> getMostPopularFilms(int limit) {
        List<Film> films = filmStorage.getFilms();
        films.sort(comparator);
        films = films.reversed();
        if (films.size() > limit) {
            return films.stream().limit(limit).toList();
        }
        return films;
    }

    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new FilmNotFoundException("Фильм с id = " + filmId + " не найден"));
    }

    private final Comparator<Film> comparator = Comparator.comparing(obj -> obj.getLikes().size());

}
