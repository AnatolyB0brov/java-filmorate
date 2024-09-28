package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DbFilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_FILMS = "SELECT f.id, f.name, f.description, f.releaseDate, f.duration, " +
            "f.rating_id, r.name AS rating_name " +
            "FROM films AS f " +
            "INNER JOIN ratings AS r ON f.rating_id = r.id ";

    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public List<Film> getFilms() {
        String sql = "ORDER BY f.id";
        return jdbcTemplate.query(SELECT_FILMS + sql, (rs, rowNum) -> getFilmFromResultSet(rs));
    }

    @Override
    public Film createFilm(Film film) {
        String sql = "INSERT INTO films (name, description, releaseDate, duration, rating_id) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, film.getName());
                    ps.setString(2, film.getDescription());
                    ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
                    ps.setInt(4, film.getDuration());
                    ps.setObject(5, film.getMpa() != null ? film.getMpa().getId() : null);
                    return ps;
                },
                keyHolder);

        film.setId(keyHolder.getKey().longValue());
        updateFilmGenres(film.getGenres(), film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        long id = film.getId();
        String sql = "UPDATE films SET name = ?, description = ?, releaseDate = ?, duration = ?, rating_id = ? " +
                "WHERE id = ?";

        Long ratingId = null;
        if (film.getMpa() != null) {
            ratingId = film.getMpa().getId();
        }
        jdbcTemplate.update(
                sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                ratingId,
                id
        );
        updateFilmGenres(film.getGenres(), id);
        return film;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        String sql = "WHERE f.id =?";
        return jdbcTemplate.query(SELECT_FILMS + sql, (rs, rowNum) -> getFilmFromResultSet(rs), filmId)
                .stream().findFirst();
    }

    @Override
    public List<Film> getMostPopularFilms(int limit) {
        String sql = "LEFT JOIN likes ON f.id = likes.film_id " +
                "GROUP BY f.id " +
                "ORDER BY COUNT(likes.film_id) DESC " +
                "FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(SELECT_FILMS + sql, (rs, rowNum) -> getFilmFromResultSet(rs), limit);
    }

    private Film getFilmFromResultSet(ResultSet resultSet) throws SQLException {
        return Film.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("releaseDate").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Rating(resultSet.getLong("rating_id"),
                        resultSet.getString("rating_name")))
                .build();
    }


    private void updateFilmGenres(List<Genre> genres, long filmId) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id =?", filmId);
        if (!genres.isEmpty()) {
            String sql = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            Genre[] g = genres.toArray(new Genre[genres.size()]);
            jdbcTemplate.batchUpdate(
                    sql,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setLong(1, filmId);
                            ps.setLong(2, g[i].getId());
                        }

                        public int getBatchSize() {
                            return genres.size();
                        }
                    }
            );
        }
    }
}
