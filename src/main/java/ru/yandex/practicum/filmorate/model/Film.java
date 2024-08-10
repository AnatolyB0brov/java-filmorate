package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotations.Release;

import java.time.LocalDate;


@Data
public class Film {
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    @Size(max = 200, message = "Описание не должно быть более 200 символов")
    private String description;

    @PastOrPresent(message = "Релиз не может быть в будущем")
    @Release(value = "1895-12-28", message = "Дата релиза не должна быть раньше 28.12.1895")
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность должна быть больше 0")
    private Integer duration;
}
