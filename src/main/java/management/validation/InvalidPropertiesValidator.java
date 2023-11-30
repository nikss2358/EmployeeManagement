package management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import management.data.PositionRepository;
import management.data.UserRepository;
import management.objectData.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ModelAttribute;

@Component
public class InvalidPropertiesValidator implements ConstraintValidator<InvalidProperties, SignInUser> {
    private UserRepository repository;

    @Autowired
    InvalidPropertiesValidator(UserRepository repository) {
        this.repository = repository;

    }
    @Override
    public boolean isValid(@ModelAttribute("signInUser") SignInUser s, ConstraintValidatorContext constraintValidatorContext) {
        User user = repository.findByUsername(s.getName());
        if (user == null)
            return false;
        return user.getPassword().equals(s.getPass());
    }
}
