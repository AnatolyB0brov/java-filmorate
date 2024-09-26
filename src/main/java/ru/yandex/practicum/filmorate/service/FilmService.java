package ru.yandex.practicum.filmorate.service;

import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exception.RatingNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeStorage likeStorage;
    private final RatingStorage ratingStorage;
    private final GenreStorage genreStorage;

    public List<Film> getFilms() {
        try {
            return filmStorage.getFilms();
        }catch (Exception e){
            e.printStackTrace();
        }
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) {
        Optional<Rating> ratingOptional = ratingStorage.getRatingById(film.getRating().getId());
        if (ratingOptional.isEmpty()) {
            throw new RatingNotFoundException("Не найден рейтинг с id = " + film.getRating().getId());
        }
        for (Genre genre : film.getGenres()) {
            Optional<Genre> optionalGenre = genreStorage.getGenreById(genre.getId());
            if (optionalGenre.isEmpty()) {
                throw new GenreNotFoundException("Не найден жанр c id = " + genre.getId());
            }
        }
        log.debug("Создание фильма: {}", film);
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        Optional<Film> optionalFilm = filmStorage.getFilmById(film.getId());
        if (optionalFilm.isPresent()) {
            log.debug("Обновление фильма: {}", film);
            return filmStorage.updateFilm(film);
        } else {
            throw new FilmNotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
    }

    public void setLike(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new FilmNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id = " + userId + " не найден");
        }
        log.info("Пользователь {} поставил лайк фильму {}", filmId, userId);
        likeStorage.setLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new FilmNotFoundException("Фильм с id = " + filmId + " не найден");
        }
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new UserNotFoundException("Пользователь с id = " + filmId + " не найден");
        }
        log.info("Пользователь {} убрал лайк фильму {}", userId, filmId);
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getMostPopularFilms(int limit) {
        if (limit <= 0) {
            throw new ValidationException("Лимит выгрузки должен быть больше 0");
        }
        return filmStorage.getMostPopularFilms(limit);
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int genreId) {
        return genreStorage.getGenreById(genreId)
                .orElseThrow(() -> new GenreNotFoundException("Жанр с id = " + genreId + " не найден"));
    }

    public List<Rating> getRatings() {
        return ratingStorage.getRatings();
    }

    public Rating getRatingById(int ratingId) {
        return ratingStorage.getRatingById(ratingId)
                .orElseThrow(() -> new RatingNotFoundException("Рейтинг с id = " + ratingId + " не найден"));
    }
}
