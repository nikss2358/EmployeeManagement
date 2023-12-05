package management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = InvalidPropertiesValidator.class)
public @interface InvalidProperties {
    String message() default "Неверное имя пользователя и/или пароль";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
