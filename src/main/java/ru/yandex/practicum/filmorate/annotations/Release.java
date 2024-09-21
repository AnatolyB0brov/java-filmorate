package ru.yandex.practicum.filmorate.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@NotNull
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseValidator.class)
public @interface Release {

    String message() default "Дата релиза не должна быть раньше {value}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};

    String value();
}
