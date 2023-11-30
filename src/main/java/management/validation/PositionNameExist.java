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
    String message() default "This name's already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
