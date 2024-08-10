package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class ReleaseValidator implements ConstraintValidator<Release, LocalDate> {
    private LocalDate min;

    @Override
    public void initialize(Release constraintAnnotation) {
        this.min = LocalDate.parse(constraintAnnotation.value());
        int i = 0;
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return !value.isBefore(min);
    }
}
