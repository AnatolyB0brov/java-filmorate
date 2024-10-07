package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class DbRatingStorage implements RatingStorage {

    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_RATINGS = "SELECT * FROM ratings ";

    public DbRatingStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Rating> getRatings() {
        return jdbcTemplate.query(SELECT_RATINGS, (rs, rowNum)
                -> new Rating(rs.getLong("id"), rs.getString("name")));
    }

    @Override
    public Optional<Rating> getRatingById(long id) {
        String sql = "WHERE id =?";
        return jdbcTemplate.query(SELECT_RATINGS + sql, (rs, rowNum)
                -> new Rating(rs.getLong("id"), rs.getString("name")), id).stream().findFirst();
    }

}
