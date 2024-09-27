package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_GENRES = "SELECT * FROM genres ";

    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query(SELECT_GENRES, (rs, rowNum)
                -> new Genre(rs.getInt("id"), rs.getString("name")));
    }

    @Override
    public Optional<Genre> getGenreById(long genreId) {
        String sql = "WHERE id = ?";
        return jdbcTemplate.query(SELECT_GENRES + sql, (rs, rowNum) -> new Genre(rs.getInt("id"),
                rs.getString("name")), genreId).stream().findFirst();
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sql = "SELECT DISTINCT genre_id, genres.name AS genre_name " +
                "FROM film_genres AS fg " +
                "INNER JOIN genres ON genres.id = fg.genre_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                rs.getString("genre_name")), filmId).stream().toList();
    }
}
