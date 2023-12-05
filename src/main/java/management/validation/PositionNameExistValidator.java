package management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import management.data.PositionRepository;
import management.objectData.Position;
import management.objectData.User;
import management.web.HomeController;
import management.web.PositionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes("user")
public class PositionNameExistValidator implements ConstraintValidator<PositionNameExist, String> {
    private final PositionRepository repository;

    @Autowired
    PositionNameExistValidator(PositionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        User user = PositionController.getUserForPositionValidation();
        Position position = repository.findByUserAndName(user, s);
        return position == null;
    }
}
