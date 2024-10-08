package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getGenres();

    Optional<Genre> getGenreById(long genreId);

    List<Genre> getGenresByFilmId(long filmId);

    List<Long> getNotExistIdsFromList(List<Long> ids);
}
