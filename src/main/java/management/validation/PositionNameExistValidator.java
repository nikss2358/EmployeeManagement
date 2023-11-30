package management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import management.data.PositionRepository;
import management.data.UserRepository;
import management.objectData.Position;
import management.objectData.User;
import org.springframework.beans.factory.annotation.Autowired;

public class PositionNameExistValidator implements ConstraintValidator<PositionNameExist, String> {
    private PositionRepository repository;

    @Autowired
    PositionNameExistValidator(PositionRepository repository) {
        this.repository = repository;

    }
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        Position position = repository.findByName(s);
        return position == null;
    }
}
