package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.annotations.Release;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private long id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не должно быть более 200 символов")
    private String description;
    @Release(value = "1895-12-28", message = "Дата релиза не должна быть раньше 28.12.1895")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность должна быть больше 0")
    private Integer duration;
    private Set<Long> likes = new HashSet<>();
    private Rating rating;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
}
