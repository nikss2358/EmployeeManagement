package management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositionNameExistValidator.class)
public @interface PositionNameExist {
    String message() default "Такое имя уже существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
